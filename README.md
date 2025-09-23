## Safe Area Experiment

> [!IMPORTANT]
> The Chromium team [promised](https://issues.chromium.org/issues/40699457#comment64) the bug has been fixed as of Chromium M140.
>
> Now it's up to the Capacitor team to correctly implement this and provide some documentation around it.
>
> See the following issue for more info: https://github.com/ionic-team/capacitor/issues/8121

### History

This plugin tried to workaround a Chromium bug that caused safe area inset variables to be incorrect. Chromium incorrectly provided the CSS env variables `env(safe-area-inset-*)` with a `0px` value. This plugin worked around that by retrieving the correct values in Kotlin and exposing them in CSS directly by using custom `var(--safe-area-inset-*)` variables. This worked alright. However, there were a few edge cases that were hard to catch and workaround correctly.

If you want to check out the old code and documentation, see the [v7 branch](https://github.com/capacitor-community/safe-area/tree/v7)
