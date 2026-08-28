#!/usr/bin/env bash
set -euo pipefail

fail=0
check() {
  if ! eval "$2"; then
    echo "❌ $1"
    fail=1
  else
    echo "✅ $1"
  fi
}

check "targetSdk is 36" "grep -Eq 'targetSdk[[:space:]]*=[[:space:]]*36' app/build.gradle.kts"
check "compileSdk is 36" "grep -Eq 'compileSdk[[:space:]]*=[[:space:]]*36' app/build.gradle.kts"
check "minSdk is at least 24" "grep -Eq 'minSdk[[:space:]]*=[[:space:]]*(2[4-9]|[3-9][0-9])' app/build.gradle.kts"
check "package id was customized" "! grep -q 'applicationId = \"com.example.universal\"' app/build.gradle.kts"
check "app name was customized" "! grep -q '<string name=\"app_name\">Universal Android App</string>' app/src/main/res/values/strings.xml"
check "privacy policy exists" "test -s policy/PRIVACY_POLICY.md"
check "Data Safety checklist exists" "test -s policy/GOOGLE_PLAY_DATA_SAFETY.md"
check "AdMob readiness checklist exists" "test -s policy/ADMOB_RELEASE_CHECKLIST.md"
check "app-ads.txt exists" "test -s developer-site/app-ads.txt"

if grep -Eq '\[(APP NAME|DEVELOPER / COMPANY|DATE|EMAIL|DESCRIBE RETENTION / ACCOUNT DELETION / DATA REQUEST PROCESS|OTHER SDKs|CONTACT DETAILS)\]' policy/PRIVACY_POLICY.md; then
  echo "❌ Privacy policy still contains template placeholders"
  fail=1
else
  echo "✅ Privacy policy placeholders were replaced"
fi

if grep -q 'pub-0000000000000000' developer-site/app-ads.txt; then
  echo "❌ app-ads.txt still contains the placeholder publisher ID"
  fail=1
else
  echo "✅ app-ads.txt publisher ID was customized"
fi

if ! grep -Eq '^google\.com, pub-[0-9]{16}, DIRECT, f08c47fec0942fa0$' developer-site/app-ads.txt; then
  echo "❌ app-ads.txt does not contain a valid Google DIRECT declaration"
  fail=1
else
  echo "✅ app-ads.txt Google DIRECT declaration format is valid"
fi

GOOGLE_TEST_PUBLISHER='3940256099942544'

if [[ -n "${ADMOB_APP_ID:-}" ]]; then
  check "production AdMob App ID format" "[[ \"$ADMOB_APP_ID\" =~ ^ca-app-pub-[0-9]{16}~[0-9]{10}$ ]]"
  if [[ "$ADMOB_APP_ID" == ca-app-pub-${GOOGLE_TEST_PUBLISHER}~* ]]; then
    echo "❌ production AdMob App ID is Google's sample/test App ID"
    fail=1
  else
    echo "✅ production AdMob App ID is not Google's sample ID"
  fi
else
  echo "❌ ADMOB_APP_ID is not supplied to release gate"
  fail=1
fi

if [[ -n "${ADMOB_BANNER_AD_UNIT_ID:-}" ]]; then
  check "production banner unit ID format" "[[ \"$ADMOB_BANNER_AD_UNIT_ID\" =~ ^ca-app-pub-[0-9]{16}/[0-9]{10}$ ]]"
  if [[ "$ADMOB_BANNER_AD_UNIT_ID" == ca-app-pub-${GOOGLE_TEST_PUBLISHER}/* ]]; then
    echo "❌ production banner unit ID is a Google sample/test ad unit"
    fail=1
  else
    echo "✅ production banner unit ID is not a Google sample ID"
  fi
else
  echo "❌ ADMOB_BANNER_AD_UNIT_ID is not supplied to release gate"
  fail=1
fi

if grep -Rqs 'ca-app-pub-3940256099942544' app/src/main; then
  echo "❌ Google test ad IDs must not be hard-coded in main source"
  fail=1
else
  echo "✅ Google test ad IDs are isolated to debug build configuration"
fi

if [[ $fail -ne 0 ]]; then
  echo
  echo "Release readiness gate FAILED. This is intentional until every production field is configured."
  exit 1
fi

echo
echo "Release readiness gate PASSED."
