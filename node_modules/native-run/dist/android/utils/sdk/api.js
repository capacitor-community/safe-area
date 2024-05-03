"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.findPackageBySchemaPath = exports.findPackageBySchema = exports.findUnsatisfiedPackages = exports.getAPILevels = void 0;
const Debug = require("debug");
const modulePrefix = 'native-run:android:utils:sdk:api';
async function getAPILevels(packages) {
    const debug = Debug(`${modulePrefix}:${getAPILevels.name}`);
    const levels = [
        ...new Set(packages.map((pkg) => pkg.apiLevel).filter((apiLevel) => typeof apiLevel !== 'undefined')),
    ].sort((a, b) => (a <= b ? 1 : -1));
    const apis = levels.map((apiLevel) => ({
        apiLevel,
        packages: packages.filter((pkg) => pkg.apiLevel === apiLevel),
    }));
    debug('Discovered installed API Levels: %O', apis.map((api) => ({ ...api, packages: api.packages.map((pkg) => pkg.path) })));
    return apis;
}
exports.getAPILevels = getAPILevels;
function findUnsatisfiedPackages(packages, schemas) {
    return schemas.filter((pkg) => !findPackageBySchema(packages, pkg));
}
exports.findUnsatisfiedPackages = findUnsatisfiedPackages;
function findPackageBySchema(packages, pkg) {
    const apiPkg = findPackageBySchemaPath(packages, pkg.path);
    if (apiPkg) {
        if (typeof pkg.version === 'string') {
            if (pkg.version === apiPkg.version) {
                return apiPkg;
            }
        }
        else {
            if (apiPkg.version.match(pkg.version)) {
                return apiPkg;
            }
        }
    }
}
exports.findPackageBySchema = findPackageBySchema;
function findPackageBySchemaPath(packages, path) {
    return packages.find((pkg) => {
        if (typeof path !== 'string') {
            return !!pkg.path.match(path);
        }
        return path === pkg.path;
    });
}
exports.findPackageBySchemaPath = findPackageBySchemaPath;
