"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.getInstalledAVDs = exports.getAVDFromINI = exports.getSDKVersionFromTarget = exports.getAVDFromConfigINI = exports.getAVDINIs = exports.isAVDConfigINI = exports.isAVDINI = void 0;
const utils_fs_1 = require("@ionic/utils-fs");
const Debug = require("debug");
const pathlib = require("path");
const ini_1 = require("../../utils/ini");
const modulePrefix = 'native-run:android:utils:avd';
const isAVDINI = (o) => o &&
    typeof o['avd.ini.encoding'] === 'string' &&
    typeof o['path'] === 'string' &&
    typeof o['path.rel'] === 'string' &&
    typeof o['target'] === 'string';
exports.isAVDINI = isAVDINI;
const isAVDConfigINI = (o) => o &&
    (typeof o['avd.ini.displayname'] === 'undefined' || typeof o['avd.ini.displayname'] === 'string') &&
    (typeof o['hw.lcd.density'] === 'undefined' || typeof o['hw.lcd.density'] === 'string') &&
    (typeof o['hw.lcd.height'] === 'undefined' || typeof o['hw.lcd.height'] === 'string') &&
    (typeof o['hw.lcd.width'] === 'undefined' || typeof o['hw.lcd.width'] === 'string') &&
    (typeof o['image.sysdir.1'] === 'undefined' || typeof o['image.sysdir.1'] === 'string');
exports.isAVDConfigINI = isAVDConfigINI;
async function getAVDINIs(sdk) {
    const debug = Debug(`${modulePrefix}:${getAVDINIs.name}`);
    const contents = await (0, utils_fs_1.readdir)(sdk.avdHome);
    const iniFilePaths = contents
        .filter((f) => pathlib.extname(f) === '.ini')
        .map((f) => pathlib.resolve(sdk.avdHome, f));
    debug('Discovered AVD ini files: %O', iniFilePaths);
    const iniFiles = await Promise.all(iniFilePaths.map(async (f) => [f, await (0, ini_1.readINI)(f, exports.isAVDINI)]));
    const avdInis = iniFiles.filter((c) => typeof c[1] !== 'undefined');
    return avdInis;
}
exports.getAVDINIs = getAVDINIs;
function getAVDFromConfigINI(inipath, ini, configini) {
    const inibasename = pathlib.basename(inipath);
    const id = inibasename.substring(0, inibasename.length - pathlib.extname(inibasename).length);
    const name = configini['avd.ini.displayname'] ? String(configini['avd.ini.displayname']) : id.replace(/_/g, ' ');
    const screenDPI = configini['hw.lcd.density'] ? Number(configini['hw.lcd.density']) : null;
    const screenWidth = configini['hw.lcd.width'] ? Number(configini['hw.lcd.width']) : null;
    const screenHeight = configini['hw.lcd.height'] ? Number(configini['hw.lcd.height']) : null;
    return {
        id,
        path: ini.path,
        name,
        sdkVersion: getSDKVersionFromTarget(ini.target),
        screenDPI,
        screenWidth,
        screenHeight,
    };
}
exports.getAVDFromConfigINI = getAVDFromConfigINI;
function getSDKVersionFromTarget(target) {
    return target.replace(/^android-(\d+)/, '$1');
}
exports.getSDKVersionFromTarget = getSDKVersionFromTarget;
async function getAVDFromINI(inipath, ini) {
    const configini = await (0, ini_1.readINI)(pathlib.resolve(ini.path, 'config.ini'), exports.isAVDConfigINI);
    if (configini) {
        return getAVDFromConfigINI(inipath, ini, configini);
    }
}
exports.getAVDFromINI = getAVDFromINI;
async function getInstalledAVDs(sdk) {
    const avdInis = await getAVDINIs(sdk);
    const possibleAvds = await Promise.all(avdInis.map(([inipath, ini]) => getAVDFromINI(inipath, ini)));
    const avds = possibleAvds.filter((avd) => typeof avd !== 'undefined');
    return avds;
}
exports.getInstalledAVDs = getInstalledAVDs;
