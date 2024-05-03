"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.run = void 0;
const json_1 = require("./utils/json");
const list_1 = require("./utils/list");
async function run(args) {
    const [ios, android] = await Promise.all([
        (async () => {
            const cmd = await Promise.resolve().then(() => require('./ios/list'));
            return cmd.list(args);
        })(),
        (async () => {
            const cmd = await Promise.resolve().then(() => require('./android/list'));
            return cmd.list(args);
        })(),
    ]);
    if (args.includes('--json')) {
        process.stdout.write((0, json_1.stringify)({ ios, android }));
    }
    else {
        process.stdout.write(`
iOS
---

${(0, list_1.formatTargets)(args, ios)}

Android
-------

${(0, list_1.formatTargets)(args, android)}

    `);
    }
}
exports.run = run;
