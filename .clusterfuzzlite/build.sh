#!/bin/bash -eu
# Copyright 2026 The APIS Common Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

# Build apis-common and a Jazzer fuzz target without modifying pom.xml.
# Mirrors patterns from https://google.github.io/oss-fuzz/getting-started/new-project-guide/jvm-lang/

PROJECT_ROOT="${SRC}/apis-common"
# Pinned apis-bom (same ref as CI in .github/workflows/ci.yml)
APIS_BOM_COMMIT="${APIS_BOM_COMMIT:-266abde1821756ee75bbbdcef1fc746bb66dcc3c}"

rm -rf "${WORK}/apis-bom"
git clone https://github.com/hyphae/apis-bom.git "${WORK}/apis-bom"
pushd "${WORK}/apis-bom" >/dev/null
git checkout "${APIS_BOM_COMMIT}"
"${MVN:-mvn}" -B -DskipTests install
popd >/dev/null

pushd "${PROJECT_ROOT}" >/dev/null
"${MVN:-mvn}" -B -DskipTests package
cp target/apis-common-*.jar "${OUT}/apis-common.jar"
mkdir -p "${OUT}/lib"
"${MVN:-mvn}" -B org.apache.maven.plugins:maven-dependency-plugin:3.6.1:copy-dependencies \
	-DoutputDirectory="${OUT}/lib" -DincludeScope=runtime
popd >/dev/null

BUILD_CP="${OUT}/apis-common.jar"
for j in "${OUT}/lib"/*.jar; do
	BUILD_CP="${BUILD_CP}:${j}"
done
BUILD_CP="${BUILD_CP}:${JAZZER_API_PATH}"

FUZZER_SRC="${PROJECT_ROOT}/fuzz/ApisCommonFuzzer.java"
javac -cp "${BUILD_CP}" -d "${OUT}" "${FUZZER_SRC}"

RUNTIME_CP=""
for j in "${OUT}/lib"/*.jar; do
	RUNTIME_CP="${RUNTIME_CP}\$this_dir/lib/$(basename "${j}"):"
done
RUNTIME_CP="${RUNTIME_CP}\$this_dir/apis-common.jar:\$this_dir"

cat >"${OUT}/ApisCommonFuzzer" <<EOF
#!/bin/bash
# LLVMFuzzerTestOneInput for fuzzer detection.
this_dir=\$(dirname "\$0")
if [[ "\$@" =~ (^| )-runs=[0-9]+($| ) ]]; then
  mem_settings='-Xmx1900m:-Xss900k'
else
  mem_settings='-Xmx2048m:-Xss1024k'
fi
LD_LIBRARY_PATH="${JVM_LD_LIBRARY_PATH}:\$this_dir" \\
\$this_dir/jazzer_driver --agent_path=\$this_dir/jazzer_agent_deploy.jar \\
--cp=${RUNTIME_CP} \\
--target_class=ApisCommonFuzzer \\
--jvm_args="\$mem_settings" \\
\$@
EOF
chmod u+x "${OUT}/ApisCommonFuzzer"
