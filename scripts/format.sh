#!/bin/bash

IGNORED_PATHS=(
    "./assets"
    "./build"
    "./analysis/build"
    "./client/build"
    "./gradle"
    "./.vscode"
    "./.idea"
    "./.git"
    "./scripts"
)

EXCLUDE_FIND=""
for path in "${IGNORED_PATHS[@]}"; do
    EXCLUDE_FIND+=" -path \"$path\" -prune -o"
done

CLANG_FORMAT_STYLE="{BasedOnStyle: LLVM, IndentWidth: 4, ColumnLimit: 120}"

eval "find . $EXCLUDE_FIND -type f \( -name '*.c' -o -name '*.h' -o -name '*.java' \) -print" | xargs clang-format --verbose --style="$CLANG_FORMAT_STYLE" --fallback-style=none -i