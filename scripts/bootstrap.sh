#!/usr/bin/env bash
set -euo pipefail

if [[ $# -ne 3 ]]; then
  echo "Usage: bash scripts/bootstrap.sh <AppName> <application.id> <package/path>"
  echo "Example: bash scripts/bootstrap.sh Rituel com.example.rituel com/example/rituel"
  exit 1
fi

APP_NAME="$1"
APP_ID="$2"
PACKAGE_PATH="$3"
OLD_PACKAGE_PATH="com/example/universal"

if [[ ! "$APP_ID" =~ ^[a-zA-Z][a-zA-Z0-9_]*(\.[a-zA-Z][a-zA-Z0-9_]*){1,}$ ]]; then
  echo "Invalid Android application id: $APP_ID"
  exit 1
fi

sed -i "s/rootProject.name = \"UniversalAndroidApp\"/rootProject.name = \"$APP_NAME\"/" settings.gradle.kts
sed -i "s/namespace = \"com.example.universal\"/namespace = \"$APP_ID\"/" app/build.gradle.kts
sed -i "s/applicationId = \"com.example.universal\"/applicationId = \"$APP_ID\"/" app/build.gradle.kts
sed -i "s#<string name=\"app_name\">Universal Android App</string>#<string name=\"app_name\">$APP_NAME</string>#" app/src/main/res/values/strings.xml

mkdir -p "app/src/main/java/$PACKAGE_PATH" "app/src/test/java/$PACKAGE_PATH"
cp -R "app/src/main/java/$OLD_PACKAGE_PATH/." "app/src/main/java/$PACKAGE_PATH/"
cp -R "app/src/test/java/$OLD_PACKAGE_PATH/." "app/src/test/java/$PACKAGE_PATH/"
find "app/src/main/java/$PACKAGE_PATH" "app/src/test/java/$PACKAGE_PATH" -type f -name '*.kt' -print0 | xargs -0 sed -i "s/com\.example\.universal/$APP_ID/g"
rm -rf "app/src/main/java/$OLD_PACKAGE_PATH" "app/src/test/java/$OLD_PACKAGE_PATH"

echo "Bootstrapped $APP_NAME ($APP_ID)."
