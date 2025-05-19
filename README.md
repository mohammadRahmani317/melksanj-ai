# 🏠 MelkSanj AI

**MelkSanj AI** is an intelligent platform for analyzing and estimating real estate prices based on ads published on [Divar](https://divar.ir/).  
This project uses Spring Boot and modern frontend tools to deliver a clean, interactive experience.

---

## 📊 Features

- 📈 Yearly price trend charts (avg. price per square meter)
- 🎯 Filtering by city, property type, and category
- 📂 Importing real estate ads from CSV files
- 🤖 AI-powered price prediction (under development)
- 🖥️ Clean and responsive UI with Chart.js & Tailwind CSS

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven
- (Optional) PostgreSQL or H2 for database

### Run the application

```bash
./mvnw spring-boot:run
```

Then open your browser and navigate to:

```
http://localhost:8080
```

---

## 🔌 REST API Endpoints

| Method | Endpoint                                  | Description                            |
|--------|-------------------------------------------|----------------------------------------|
| POST   | `/api/melksanj/data/import`              | Import real estate ads from CSV        |
| GET    | `/api/melksanj/price/sale/yearly`        | Yearly average prices (by filters)     |
| GET    | `/api/melksanj/price/sale/monthly`       | Monthly avg. prices for selected year  |
| GET    | `/api/melksanj/meta/cities`              | List of available cities               |
| GET    | `/api/melksanj/meta/groups`              | List of property groups                |
| GET    | `/api/melksanj/meta/categories`          | List of property categories            |
| GET    | `/api/melksanj/meta/years`               | Available years based on data          |

---

## 🧰 Tech Stack

- **Backend:** Spring Boot (Java 17)
- **Frontend:** HTML + Tailwind CSS + Chart.js
- **Data Processing:** Apache Commons CSV
- **Date Handling:** Persian Date library
- **Database:** PostgreSQL / H2 (switchable)

---

## 📁 Project Structure (Simplified)

```
src/main/java/com/melksanj
├── service         # Business logic and data analysis
├── web             # REST controllers
├── model           # JPA entities and DTOs
├── repository      # Spring Data JPA repositories

src/main/resources/static
├── index.html          # Landing page
├── analytics.html      # Price charts
├── predict.html        # AI price prediction (WIP)
```

---

## 📈 Planned Features

- Price prediction using ML models (regression & transformer-based)
- Comparative charts between multiple cities
- Admin dashboard for managing and monitoring listings

---

## 👨‍💻 Author

Developed by **Mohammad Rahmani** – 2025 ©  
Feel free to contribute or fork!