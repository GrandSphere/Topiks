#!/bin/bash

# Script to replace 'package com.example.*' with 'package com.grandsphere.*' in all files

# Define the old and new package prefixes
OLD_PREFIX="GrandSphere.topics"
NEW_PREFIX="com.GrandSphere"

# Find all files containing the old package prefix
echo "Searching for files containing 'import $OLD_PREFIX'..."
grep -rl --include=\*.{java,kt,xml} "$OLD_PREFIX" . > temp.txt

# Optional: Clean and rebuild the project (uncomment if needed)
# echo "Cleaning and rebuilding the project..."
# ./gradlew clean build
