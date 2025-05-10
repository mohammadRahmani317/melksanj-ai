# MelkSanj AI

سامانه‌ای هوشمند برای تحلیل و تخمین قیمت املاک بر اساس آگهی‌های منتشر شده در دیوار.

## 📊 ویژگی‌ها

* نمایش نمودار تغییرات میانگین قیمت املاک به تفکیک سال
* فیلتر بر اساس شهر، نوع آگهی، و نوع ملک
* CSV بارگذاری داده‌های آگهی از فایل
* تخمین قیمت ملک با استفاده از مدل هوش مصنوعی (در حال توسعه)
* رابط کاربری ساده و زیبا با Chart.js و Tailwind CSS

## 🚀 راه‌اندازی پروژه

### پیش‌نیازها

* Java 17 یا بالاتر
* Maven
* (اختیاری) Postgres یا H2 Database

### اجرای پروژه

```bash
./mvnw spring-boot:run
```

سپس مرورگر را باز کرده و به آدرس زیر بروید:

```
http://localhost:8080
```

## 🔗 REST API Endpointها

| متد  | مسیر                                    | توضیح                             |
| ---- | --------------------------------------- |-----------------------------------|
| POST | `/api/melksanj/data/import`             | csv بارگذاری داده‌ها از فایل      |
| GET  | `/api/melksanj/price/yearly?cityId=...` | دریافت میانگین قیمت به تفکیک سال  |
| GET  | `/api/melksanj/meta/cities`             | لیست شهرها                        |
| GET  | `/api/melksanj/meta/groups`             | لیست نوع آگهی‌ها                  |
| GET  | `/api/melksanj/meta/categories`         | لیست دسته‌بندی املاک              |

## 🧰 تکنولوژی‌های استفاده شده

* Spring Boot
* Java 17
* Chart.js
* Tailwind CSS
* CSVParser (Apache Commons)
* Persian Date Library

## 📂 ساختار پروژه (خلاصه)

```text
src/main/java/com/melksanj
├── service         ← منطق بارگذاری و تحلیل داده‌ها
├── web             ← کنترلرهای REST API
├── model           ← مدل‌های داده‌ای (Entity)
├── repository      ← ارتباط با دیتابیس

src/main/resources/static
├── index.html      ← صفحه اصلی
├── charts.html     ← نمودارها
├── predict.html    ← تخمین قیمت
```

## ✨ توسعه در آینده

* ML مدل پیش‌بینی قیمت با استفاده از مدل‌های 
* اضافه کردن نمودارهای مقایسه‌ای بین شهرها
* صفحه ادمین برای مدیریت و مشاهده دیتا

---

© 2025 MelkSanj AI – توسعه داده شده توسط محمد رحمانی
