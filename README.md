<h1 align="center">AppUpdater </h1>
<h4 align="center">Android Library</h4>

This module is reference javiersantos's AppUpdater
<a href="https://github.com/javiersantos/AppUpdaterr">javiersantos's AppUpdater</a>

## How to use
1. Import the library module to your project:
```
a. Click File > New > Import Module.
b. Enter the location of the library module directory then click Finish.
```

2. And add the library to your module **settings.gradle**:
```
include ':app', ':appupdater'
```
3. And add the library to your module build.gradle:
```
dependencies {
    implementation project(':appupdater')
}
```
## Usage
Use AppUdpater.getLatestAppVersion method to get app page information,
you can use callback in new AppUpdater.OnFinishListener() to process result
**note**
a. the call back is in the work thread
b. if the app is not in googlplay, the version will be empty

        AppUpdater.getLatestAppVersion(this, new AppUpdater.OnFinishListener() {
            @Override
            public void onSuccess(Update update) {
                // use update to get infor mation
                Log.d(TAG, "getLatestVersion " + update.getLatestVersion());
                Log.d(TAG, "getReleaseNotes " + update.getReleaseNotes());
                Log.d(TAG, "getUrlToDownload " + update.getUrlToDownload());
            }

            @Override
            public void onFailed(String errorMessage) {
                // when error  to do somethig
                Log.d(TAG, "ErrorMessage =  " + errorMessage);
            }
        });

## License
	Copyright 2018 RX1226

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
