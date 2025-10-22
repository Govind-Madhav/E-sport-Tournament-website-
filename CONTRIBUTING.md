# Contributing

Thanks for contributing! A few notes specific to this repository are below to make onboarding and collaboration smooth.

## Quick start for contributors (after cloning)
1. Clone the repository:

```powershell
git clone https://github.com/Govind-Madhav/E-sport-Tournament-website-.git
cd "E-sport tournament website project"
```

2. Install Git LFS (required to fetch large binary files):

```powershell
# using Chocolatey (if you have it)
choco install git-lfs -y
# or download from https://git-lfs.github.com/

git lfs install
```

3. Fetch LFS objects:

```powershell
git lfs pull
```

## If you already had a clone before the LFS migration
This repository's history was rewritten to move large files into Git LFS. To avoid problems, re-clone the repository. If you must keep your existing clone, you can reset it to the rewritten history (danger: this will discard local changes):

```powershell
git fetch origin
git reset --hard origin/main
git lfs install
git lfs pull
```

## Backup branch
A backup of the pre-migration state is available on the remote as `backup-before-lfs-migration`. If you need to inspect or restore the old history, you can fetch it:

```powershell
git fetch origin backup-before-lfs-migration:backup-before-lfs-migration
git checkout backup-before-lfs-migration
```

## Notes
- Use `git lfs track` for new large binary files (images, executables). Add `.gitattributes` entries and commit them.
- Avoid committing very large files (>100MB). Use LFS or external storage for those.
- If you are unsure, ask before force-pushing: rewriting history will affect other contributors.

Thanks — and happy hacking!