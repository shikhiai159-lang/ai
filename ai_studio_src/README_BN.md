# AI Studio Pro — reconstructed source bundle

এই ZIP-এ আপনার দেওয়া APK থেকে উদ্ধার করা `assets/public/index.html`-এর সোর্স এবং Android Studio/Capacitor-এর পুনর্গঠিত প্রজেক্ট কাঠামো রাখা হয়েছে। এটি **মূল ডেভেলপারের সম্পূর্ণ সোর্স কোড নয়**—APK থেকে আসল Kotlin/Java ফাইল, Gradle configuration, signing key বা build history পুরোপুরি ফেরত পাওয়া যায় না।

## গুরুত্বপূর্ণ: Ollama এখনো APK-তে এমবেড করা নেই

আপনার মূল UI `http://localhost:11434`-এ চলমান Ollama সার্ভারকে কল করে। নতুন Android প্রজেক্টে একটি foreground service scaffold যোগ করা হয়েছে, কিন্তু **সঠিক Android/ARM64-সামঞ্জস্যপূর্ণ Ollama executable এবং তার নির্ভরশীল লাইব্রেরি এই ZIP-এ নেই**। তাই এই ZIP থেকে তৈরি APK এখনই Termux ছাড়া `gemma4:cloud` চালাবে—এমন দাবি করা হচ্ছে না। সাধারণ Linux Ollama binary কপি করলেই Android-এ চলবে না।

`android/app/src/main/assets/ollama/` ফোল্ডারটি ইচ্ছাকৃতভাবে রাখা হয়েছে। সেখানে পরীক্ষা-করা Android-compatible executable ও প্রয়োজনীয় runtime যুক্ত না হওয়া পর্যন্ত সার্ভিসটি শুধু runtime missing বলে notification দেখাবে।

## বিল্ডের জন্য

1. Android Studio, JDK 17, Android SDK (API 35) ইনস্টল করুন।
2. এই ফোল্ডারটি Android Studio-তে খুলুন।
3. প্রজেক্টে Capacitor Android dependency sync করুন (Node.js/npm লাগবে):
   - প্রজেক্টের root-এ `npm install`
   - `npx cap add android` কেবল তখনই চালাবেন, যদি `android` প্ল্যাটফর্মের Capacitor-generated ফাইল না থাকে। এই bundle-এ Android app scaffold আছে, তাই existing `android` folder overwrite করবেন না।
4. Android Studio-তে Gradle sync/build করুন। Build tooling/version mismatch হলে Android Studio-র দেখানো Gradle/AGP compatibility অনুসারে সংশোধন লাগতে পারে।

## `gemma4:cloud` এবং বিনামূল্যের ব্যবহার

অ্যাপের UI-তে মডেল নাম `gemma4:cloud` রাখা হয়েছে। Cloud model-এর জন্য Ollama-র সাইন-ইন ও সার্ভার runtime দরকার হতে পারে; free allowance অ্যাকাউন্ট/নীতি অনুযায়ী সীমিত হতে পারে। এই সোর্সে কোনো paid API key রাখা হয়নি এবং কোনো API key hardcode করা উচিত নয়।

## মূল ফাইল

- `www/index.html` — APK থেকে উদ্ধার করা AI Studio Pro UI ও JavaScript
- `capacitor.config.ts`, `package.json` — Capacitor project config
- `android/app/src/main/java/.../MainActivity.kt` — app entry point
- `android/app/src/main/java/.../OllamaService.kt` — অসম্পূর্ণ runtime-launcher scaffold

এই archive একটি **source/reconstruction starting point**, ready-to-install patched APK নয়।
