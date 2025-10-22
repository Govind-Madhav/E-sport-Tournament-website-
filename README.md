🎮 Esports Tournament Website (Monorepo)

A full-stack Esports tournament website with Java Spring Boot backend and Vite + React frontend. Organized under Esports Tournament Website/CODE/.

🗂 Project Layout

Backend (Esports Tournament Website/CODE/BACKEND/esports-tournament/)

pom.xml — Maven configuration

mvnw / mvnw.cmd — Maven wrapper

src/main/java/com/esports/tournament/ — Java source code

src/main/resources/ — application.properties, static assets, templates

Frontend (Esports Tournament Website/CODE/FRONTEND/esports/)

package.json — dependencies & scripts

vite.config.js, tailwind.config.js, postcss.config.js

src/ — React components and source code

public/ — public static assets

⚡ Features

User registration and login for players

Tournament creation and scheduling

Real-time match updates and leaderboard

Fully responsive frontend with Tailwind CSS

RESTful API powered by Spring Boot

Easy local setup for development

🛠 Tech Stack

Backend: Java, Spring Boot, Maven

Frontend: React, Vite, Tailwind CSS

Database: MySQL (or configured in application.properties)

Other: Git, Git LFS for large assets

🚀 Quick Start (Windows / PowerShell)

Prerequisites: Java 11+, Node.js 16+, Git

Backend Dev Server:

cd "Esports Tournament Website/CODE/BACKEND/esports-tournament"
.\mvnw.cmd spring-boot:run


Build Backend JAR:

cd "Esports Tournament Website/CODE/BACKEND/esports-tournament"
.\mvnw.cmd clean package -DskipTests
# JAR will be in target/


Frontend Dev Server:

cd "Esports Tournament Website/CODE/FRONTEND/esports"
npm install
npm run dev
# Open URL shown by Vite


Build Frontend for Production:

cd "Esports Tournament Website/CODE/FRONTEND/esports"
npm install
npm run build
# dist/ contains production-ready files

⚠️ Notes

Large binaries (images, JDK) are included; consider using Git LFS.

Backend and frontend can run concurrently in separate terminals.

🛠 Troubleshooting

Maven wrapper not working → install Maven and use:

mvn spring-boot:run


Check application.properties for DB URL, server port, etc.

🙌 Authors / Credits

Govind Madhav — Lead Developer, Planner, Designer

Ayush Kulasari — Financial Support

📄 License

This project is open for internal use. Update or include a proper license if sharing publicly.
