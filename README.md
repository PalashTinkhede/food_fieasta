# 🍽️ Food Fiesta - Premium Dining Management

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-latest-blue.svg)](https://www.postgresql.org/)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D.svg)](https://swagger.io/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Food Fiesta** is a premium, full-stack dining management system built to streamline restaurant operations. From inventory tracking to real-time order processing, it provides a robust platform for both administrators and customers.

---

## 🚀 Key Features

- **🛡️ Secure Authentication**: Spring Security form login with BCrypt-hashed
  passwords and role-based access control - `/admin/**` is restricted to the
  `ADMIN` role, and every state-changing request is a CSRF-protected POST.
- **📦 Inventory Management**: Full CRUD operations with categorized items (Biryani, North Indian, Chinese, etc.).
- **🥘 Diverse Catalog**: Support for multiple cuisines including Tandoori, Paneer delicacies, and Desserts.
- **🛒 Order Lifecycle**: Seamless order placement, tracking, and history management.
- **📄 API Documentation**: Interactive Swagger/OpenAPI documentation for all system endpoints.
- **🎨 Modern UI**: A unified, token-driven design system (`main.css`) with a glassmorphism navbar, card-based layouts, smooth micro-animations, and fully responsive pages — shared across the public site, user ordering flow, and admin dashboard.
- **📊 Admin Dashboard**: Comprehensive overview of users, products, and total orders.

---

## 🛠️ Technology Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Java 21, Spring Boot 3.4.2, Spring Security, Hibernate |
| **Database** | PostgreSQL |
| **Documentation** | SpringDoc OpenAPI (Swagger) |
| **Frontend** | Thymeleaf, single design-system stylesheet (`main.css`, CSS custom properties), Font Awesome 6, Plus Jakarta Sans |
| **Build Tool** | Maven |

---

## 📂 Suggested Folder Structure

For a scalable enterprise-grade application, we recommend the following structure:

```bash
src/main/java/com/example/demo/
├── 📁 common/             # Shared constants, enums, Utility classes
├── 📁 config/             # Security, OpenAPI, and App configurations
├── 📁 controllers/        # REST and MVC Controllers
├── 📁 dto/                # Data Transfer Objects (Request/Response)
├── 📁 entities/           # Database Models (JPA Entities)
├── 📁 exceptions/         # Global Exception handling logic
├── 📁 repository/         # Data Access Layer (Spring Data JPA)
├── 📁 security/           # JWT, UserDetails, and Filter logic
└── 📁 services/           # Business Logic Layer
    └── 📁 impl/           # Service Implementations
```

---

## 🎨 UI Design System

Every page in the app — public, user, and admin — is styled by a single stylesheet,
`src/main/resources/static/css/main.css`, built on CSS custom properties (design tokens):

- **Tokens**: brand orange (`--primary: #c2410c`) with light/container variants, surface and
  outline colors, shadows, and a shared transition curve — change the look app-wide in one place.
- **Typography**: [Plus Jakarta Sans](https://fonts.google.com/specimen/Plus+Jakarta+Sans) with
  tight letter-spacing on headings and fluid `clamp()` sizing for hero text.
- **Components**: a floating glassmorphism navbar, pill buttons (`.btn-primary` / `.btn-outline`),
  glass cards (`.card-glass`), dashboard panels (`.panel`), styled data tables (`.data-table`)
  with hover states and icon action buttons, and auth/form cards (`.auth-card`, `.input-field`).
- **Feedback**: `.alert-error` / `.alert-success` banners and a slide-up entrance animation
  (`.animate-up`).
- **Responsive**: fluid layouts, horizontal-scroll table wrappers, and mobile breakpoints
  throughout — no page-specific stylesheets remain.

---

## 🌟 Features to Add (Roadmap)

1.  **💳 Payment Gateway**: Integrate Stripe or Razorpay for online transactions.
2.  **🔑 JWT Authentication**: Transition to stateless JWT for mobile-app readiness.
3.  **📸 Image Storage**: AWS S3 integration for hosting high-quality food images.
4.  **🔔 Real-time Alerts**: WebSocket integration for instant order status notifications.
5.  **📈 Analytics Suite**: Visualized sales reports using Chart.js or D3.js.
6.  **📧 Notification Service**: Automated email invoices and promotional newsletters.

---

## 🖥️ Application Workflow & Pages

### 🏠 Public Access
- **Home (`Home.html`)**: Landing page with promotional banners and featured items.
- **Menu/Products (`Products.html`)**: Interactive catalog organized by categories:
  - 🍛 **Biryani**: Hyderabadi, Lucknowi, and Kolkata specialties.
  - 🍗 **Chicken & Tandoori**: Matka Chicken, Tikka, and Butter Chicken.
  - 🍜 **Chinese**: Chowmein, Manchurian, and Momos.
  - 🧀 **Paneer Delights**: Shahi Paneer, Kadai Paneer, and Matar Paneer.
  - 🥗 **Vegetables & North Indian**: Soya Chaap, Chola Bhatura, and more.
- **Location (`Locate_us.html`)**: Google Maps integration and restaurant address details.
- **About (`About.html`)**: Restaurant story, team, and mission.

### 👤 User Module
- **Registration (`register.html`)**: Account creation for new customers.
- **Login (`Login.html`)**: Secure entry for users and administrators.
- **Ordering (`BuyProduct.html`)**: Selection and quantity management for orders.
- **Success (`Order_success.html`)**: Order confirmation and summary.

### 🔐 Admin Dashboard
- **Management Center (`Admin_Page.html`)**: Central hub for monitoring users, stock, and orders.
- **Entities CRUD**:
  - `Add_Product.html` / `Update_Product.html`
  - `Add_User.html` / `Update_User.html`
  - `Add_Admin.html` / `Update_Admin.html`

### 🛠️ Shared Components & Utilities
- **Navigation (`Navigation.html`)**: Universal header with dynamic links based on user role.
- **Error Handling (`exception.html`)**: Custom design for handling and displaying system exceptions.

---

## 📸 Interface Preview

<details>
  <summary><b>Click to view all Application Screens</b></summary>
  <br>
  
  <p align="center">
    <img src="https://github.com/user-attachments/assets/1382d32f-3cbb-40c3-b6b5-9fc55cd5176f" width="400">
    <img src="https://github.com/user-attachments/assets/3e34f54c-c986-42ac-96a4-ed7ad18035a6" width="400">
  </p>
  
  <p align="center">
    <img src="https://github.com/user-attachments/assets/a4046d4e-8c3d-4629-8913-5543d709e80e" width="400">
    <img src="https://github.com/user-attachments/assets/09c92348-ec06-4607-9ae4-88b28cc1e0ec" width="400">
  </p>

  <p align="center">
    <img src="https://github.com/user-attachments/assets/0e73aece-a28c-413d-9363-245358a0e439" width="400">
    <img src="https://github.com/user-attachments/assets/ac26bd92-0fcd-473d-a175-9bc45fdb12ba" width="400">
  </p>

  <p align="center">
    <img src="https://github.com/user-attachments/assets/3d407958-efbe-449f-b4e1-5b08b9f8a28b" width="400">
    <img src="https://github.com/user-attachments/assets/c11a4710-69f8-42fd-b9d7-2b5278b2c8a3" width="400">
  </p>

  <p align="center">
    <img src="https://github.com/user-attachments/assets/d3cd3cdd-cda5-460a-a253-24e45cf600b0" width="400">
    <img src="https://github.com/user-attachments/assets/733afb94-a251-4a6d-86a4-3a124e8c3469" width="400">
  </p>

  <p align="center">
    <img src="https://github.com/user-attachments/assets/ef09886f-7936-4ef7-b01e-4da0008fd047" width="400">
    <img src="https://github.com/user-attachments/assets/219272b8-3938-4b7f-ba78-817e507beee9" width="400">
  </p>
  
  <p align="center">
    <img src="https://github.com/user-attachments/assets/d4da51a4-dfe8-4cc1-ae49-745f6e1ae17a" width="400">
    <img src="https://github.com/user-attachments/assets/09c31cf7-e960-4ea5-a858-8807317486b7" width="400">
  </p>

  <p align="center">
    <img src="https://github.com/user-attachments/assets/b1da1faa-7e71-49be-972a-432029a1e7c3" width="400">
    <img src="https://github.com/user-attachments/assets/b0a9a1e2-6018-475a-95fc-d64980677ee7" width="400">
  </p>

  <p align="center">
    <img src="https://github.com/user-attachments/assets/b860f905-2c35-4af3-9df8-38cda70b4406" width="400">
  </p>
</details>

---

## ⚙️ Setup and Installation

### Prerequisites
- JDK 21+
- PostgreSQL
- Maven 3.8+

### Steps
1. **Clone the Repo**
   ```bash
   git clone https://github.com/yourusername/food-fiesta.git
   ```
2. **Configure Database**
   Credentials are read from the environment - nothing secret is committed to
   `application.properties`. Set these before starting the app:

   | Variable | Purpose | Default |
   | :--- | :--- | :--- |
   | `DB_URL` | JDBC connection string | `jdbc:postgresql://localhost:5432/foodfiesta` |
   | `DB_USERNAME` | Database user | `postgres` |
   | `DB_PASSWORD` | Database password | *(empty)* |
   | `DDL_AUTO` | Hibernate schema mode | `update` |
   | `SHOW_SQL` | Log generated SQL | `false` |
   | `FOODFIESTA_ADMIN_EMAIL` | Seeds the first admin account | *(unset - no seeding)* |
   | `FOODFIESTA_ADMIN_PASSWORD` | Password for that admin | *(unset - no seeding)* |

   ```bash
   export DB_PASSWORD='your_password'
   export FOODFIESTA_ADMIN_EMAIL='admin@foodfiesta.com'
   export FOODFIESTA_ADMIN_PASSWORD='choose-a-strong-one'
   ```

3. **Build & Run**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Sign In**
   A fresh database has no accounts. Set the two `FOODFIESTA_ADMIN_*` variables
   above and the first admin is created on startup; sign in with them at
   `/login`, then use the dashboard to add more staff. Customers self-register
   at `/register`.

   Passwords are stored as BCrypt hashes. Any rows left over from an older
   build that stored plaintext are re-hashed in place on the next startup.

5. **Access UI**
   - Home: `http://localhost:8080/`
   - Swagger Docs: `http://localhost:8080/swagger-ui/index.html`

---

## 🤝 Contributing
Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git origin push feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.

---
*Developed with ❤️ in 2026 by the Food Fiesta Team*
