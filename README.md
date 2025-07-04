# Function Plotter

- Using the [LVP](https://github.com/denkspuren/LiveViewProgramming) application.

# How to use

## 1. Clone the repository

```bash
git clone https://github.com/Ole-THM/LVP-Extended
cd LVP-Extended
```

## 2. Run the application

```bash
./run.sh
```

- This will start the application when ran in a Linux environment. (On Windows, you could use GitBash or Powershell)

- Alternatively, you can build and run the application manually:


1. Build the project using Maven:
```bash
mvn clean package
```

2. Run the application using the generated JAR file:
```bash
java -jar ./src/main/java/lvp-0.5.4.jar --log --watch=./src/main/java/start.java
```
