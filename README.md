# Esports Tournament Website (Monorepo)

This repository contains a Java (Maven / Spring Boot) backend and a Vite + React frontend for an Esports tournament website. The code is organized under `Esports Tournament Website/CODE/`.

## Project layout
- `Esports Tournament Website/CODE/BACKEND/esports-tournament/` — Java backend (Maven)
  - `pom.xml` — Maven configuration
  - `mvnw` / `mvnw.cmd` — Maven wrapper
  - `src/main/java/com/esports/tournament/` — Java source code
  - `src/main/resources/` — `application.properties`, `static/` assets, `templates/`
- `Esports Tournament Website/CODE/FRONTEND/esports/` — React frontend (Vite)
  - `package.json` — frontend dependencies & scripts
  - `vite.config.js`, `tailwind.config.js`, `postcss.config.js`
  - `src/` — React source and components
  - `public/` — public static images

## Quick start (Windows / PowerShell)

Prerequisites:
- Java 11+ (or the version required by the backend's `pom.xml`)
- Node.js 16+ and npm (or yarn)
- Git

Backend (run):

```powershell
cd "Esports Tournament Website/CODE/BACKEND/esports-tournament"
# Use the wrapper on Windows
.\\mvnw.cmd spring-boot:run
```

Build backend JAR:

```powershell
cd "Esports Tournament Website/CODE/BACKEND/esports-tournament"
.\\mvnw.cmd clean package -DskipTests
# jar will be in target/ (e.g. target/*.jar)
```

Frontend (run dev server):

```powershell
cd "Esports Tournament Website/CODE/FRONTEND/esports"
npm install
npm run dev
# open the URL shown by Vite (usually http://localhost:5173)
```

Build frontend for production:

```powershell
cd "Esports Tournament Website/CODE/FRONTEND/esports"
npm install
npm run build
# built files go into dist/ by default
```

## Notes and recommendations
- Large binary images are currently included in the repo under backend `src/main/resources/static/images` and frontend `public/`. If you prefer not to store large binaries in Git history, consider using Git LFS or moving them to external storage.
- The repository's `main` branch is configured as the upstream remote `origin` (GitHub)
- You can run backend and frontend concurrently in two separate terminals when developing.

## Troubleshooting
- If Maven wrapper doesn't run, install a local Maven and run `mvn spring-boot:run`.
- Check `application.properties` for backend configuration (database URL, server port, etc.).

---
Created and added by automation. Update this README with project-specific commands and environment variables as needed.
