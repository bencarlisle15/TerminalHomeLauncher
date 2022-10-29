-keep public class com.bencarlisle15.terminalhomelauncher.commands.main.raw.* { public *; }
-keep public abstract class com.bencarlisle15.terminalhomelauncher.commands.main.generals.* { public *; }
-keep public class com.bencarlisle15.terminalhomelauncher.commands.tuixt.raw.* { public *; }
-keep public class com.bencarlisle15.terminalhomelauncher.managers.notifications.NotificationService
-keep public class com.bencarlisle15.terminalhomelauncher.managers.notifications.KeeperService
-keep public class com.bencarlisle15.terminalhomelauncher.managers.options.**
-keep class com.bencarlisle15.terminalhomelauncher.tuils.libsuperuser.**
-keep class com.bencarlisle15.terminalhomelauncher.managers.suggestions.HideSuggestionViewValues
-keep public class it.andreuzzi.comparestring2.**

-dontwarn com.bencarlisle15.terminalhomelauncher.commands.main.raw.**

-dontwarn javax.annotation.**
-dontwarn javax.inject.**

-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-dontwarn org.htmlcleaner.**
-dontwarn com.jayway.jsonpath.**
-dontwarn org.slf4j.**

-dontwarn org.jdom2.**