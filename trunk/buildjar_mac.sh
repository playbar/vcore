rm -rf output
mkdir output
cd output
mkdir assets
cd ..
cp viewcore/build/intermediates/classes/release output/ /s
cp viewcore/build/intermediates/bundles/release/assets output/assets/ /s
rm -rf output/com/bfmj/viewcore/R*.*
cd output
jar cf MJViewCore.jar .
#
#
#mkdir arm
#cd ..
#xcopy viewcore/build/intermediates/bundles/release/jni/armeabi output/arm/ /s