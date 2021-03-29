fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew install fastlane`

# Available Actions
### checkout_develop
```
fastlane checkout_develop
```
Checkouts to development branch
### validate_version_name
```
fastlane validate_version_name
```
Checks if a version name follows the pattern major.minor.patch
### bump_version_name
```
fastlane bump_version_name
```
Bumps a version name following the pattern major.minor.patch
### commit_versioning
```
fastlane commit_versioning
```
Adds changed versioning files to git, and commit
### tag_versioning
```
fastlane tag_versioning
```
Tags the current branch and push to remote

----

## Android
### android test
```
fastlane android test
```
Runs all the tests
### android release
```
fastlane android release
```
Prepares a release version

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).
