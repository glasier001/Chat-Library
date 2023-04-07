# Chat-Library
A Simple Library For Chat Integration
How to implement the library in the other project

Step 1

Add the below lines to the settings.gradle

replace the dependency release block by this

dependencyResolutionManagement {
repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
repositories {
google()
mavenCentral()
maven {
url "https://github.com/jitsi/jitsi-maven-repository/raw/master/releases"
}
maven {
url "https://maven.google.com"
}
maven { url 'https://www.jitpack.io' }
jcenter()
}
}

Step 2
Add the below lines in build.gradle app level
in android block add the fllowing code
packagingOptions {
exclude 'META-INF/DEPENDENCIES'
pickFirst 'lib/x86/libc++_shared.so'
pickFirst 'lib/x86_64/libc++_shared.so'
pickFirst 'lib/armeabi-v7a/libc++_shared.so'
pickFirst 'lib/arm64-v8a/libc++_shared.so'
}
buildFeatures {
dataBinding true
// for view binding:
// viewBinding true
}

add the below line in dependency block
implementation 'com.github.glasier001:Chat-Library:0.1.81’

Step 3 change the theme to NoActionBar

Step 4 in manifest file add these lines in application tag
tools:replace="android:theme"
android:usesCleartextTraffic=“true”

Step 5
add the below line in gradle.properties file
android.enableJetifier=true

Step 6
Use it where you want like
Intent intent = new Intent(this, ClassRoomListActivity.class);
intent.putExtra("classuserId", "4200");
intent.putExtra("classUserEmail", "studentdeveloper1@test.com");
intent.putExtra("classuserType","learner");
startActivity(intent);
