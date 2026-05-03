#!/bin/bash -eu


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
