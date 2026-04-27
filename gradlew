#!/usr/bin/env sh
# Lightweight Gradle launcher for GitHub Actions.
# setup-gradle installs the gradle command, then this file forwards commands to it.
exec gradle "$@"
