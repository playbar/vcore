rm -rf output
md output
cd output
md assets
cd ..
cp viewcore\build\intermediates\classes\release output\ /s
cp viewcore\build\intermediates\bundles\release\assets output\assets\ /s
rm -rf output\com\bfmj\viewcore\R*.*
cd output
jar cf MJViewCore.jar .
