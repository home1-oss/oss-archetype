#!/usr/bin/env bash

# decrypt some files before build
#
#
#

export BUILD_SITE="false"





### OSS CI CALL REMOTE CI SCRIPT BEGIN
if [ -z "${LIB_CI_SCRIPT}" ]; then LIB_CI_SCRIPT="https://github.com/home1-oss/oss-build/raw/develop/src/main/ci-script/lib_ci.sh"; fi
echo "eval \$(curl -s -L ${LIB_CI_SCRIPT})"
eval "$(curl -s -L ${LIB_CI_SCRIPT})"
### OSS CI CALL REMOTE CI SCRIPT END
