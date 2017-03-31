rd /s/q output
mkdir output
cd output
mkdir assets
cd ..
xcopy viewcore\build\intermediates\classes\release output\ /s
xcopy viewcore\build\intermediates\bundles\release\assets output\assets\ /s
del /f/s/q output\com\bfmj\viewcore\R*.*
cd output
jar cf ..\MJViewCore.jar .
mkdir arm
cd ..
xcopy viewcore\build\intermediates\bundles\release\jni\armeabi output\arm\ /s
