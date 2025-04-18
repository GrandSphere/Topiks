#!/bin/bash

# Script to replace 'package com.example.*' with 'package com.grandsphere.*' in all files

# Define the old and new package prefixes
OLD_PREFIX="com.GrandSphere.topics"
NEW_PREFIX="com.GrandSphere.Topiks"

# Find all files containing the old package prefix
echo "Searching for files containing 'import $OLD_PREFIX'..."
files=$(grep -rl --include=\*.{java,kt,xml} "package $OLD_PREFIX" .)

if [ -z "$files" ]; then
    echo "No files found with 'package $OLD_PREFIX'."
    exit 0
fi

# Loop through each file and perform the replacement
for file in $files; do
    echo "Processing $file..."
    # Use sed to replace 'package com.example.' with 'package com.grandsphere.'
    sed -i "s/package $OLD_PREFIX\./package $NEW_PREFIX\./g" "$file"
done

echo "Replacement complete!"
echo "Please review the changes and test your project."

# Optional: Clean and rebuild the project (uncomment if needed)
# echo "Cleaning and rebuilding the project..."
# ./gradlew clean build
