FROM gcr.io/oss-fuzz-base/base-builder-jvm
RUN apt-get update && apt-get install -y maven git \
	&& rm -rf /var/lib/apt/lists/*
COPY . $SRC/apis-common
WORKDIR $SRC/apis-common
COPY .clusterfuzzlite/build.sh $SRC/build.sh
