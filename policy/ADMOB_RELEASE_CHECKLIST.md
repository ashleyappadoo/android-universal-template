# AdMob release checklist

- [ ] App is registered in AdMob and linked to the final Play Store package.
- [ ] Production AdMob App ID is supplied through GitHub Actions variables/secrets.
- [ ] Production ad-unit IDs are supplied through GitHub Actions variables/secrets.
- [ ] Google sample/test IDs are used only in debug builds.
- [ ] UMP consent information is refreshed at app launch.
- [ ] Ads are requested only when `canRequestAds()` permits it.
- [ ] A privacy options entry point is exposed when UMP reports it as required.
- [ ] Ad placements comply with Google publisher policies.
- [ ] The Play Console declares that the app contains ads.
- [ ] A developer website is present in the Play Store listing.
- [ ] `app-ads.txt` is published at the ROOT of that developer website domain.
- [ ] `app-ads.txt` uses the real AdMob publisher ID and has been validated in AdMob.
- [ ] AdMob app-readiness review is approved before expecting unrestricted ad serving.
- [ ] Child-directed / under-age settings are reviewed if the audience can include children.
- [ ] Privacy policy and Data Safety declarations match the release binary.
