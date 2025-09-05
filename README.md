# **JSON ↔ PHP Array Converter Plugin**
A **PhpStorm plugin** that converts **JSON to PHP array** and **PHP array to JSON**.  
The plugin provides a **tool window** for JSON input and output, allowing easy bidirectional conversion and replacement within the editor.

✅ **Tested on:**
- **Java 21**
- **PhpStorm 2024.2.5**

---

## **Installation & Setup**
### **1. Install Java 21**
Ensure that **Java 21** is installed. If not, install it via **Homebrew** (macOS) or manually.

#### **Check Installed Java Versions**
```sh
/usr/libexec/java_home -V
```
If **Java 21** is missing, install it:

#### **macOS (Homebrew)**
```sh
brew install openjdk@21
sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
```

#### **Ubuntu (APT)**
```sh
sudo apt update
sudo apt install openjdk-21-jdk
```

#### **Windows (Scoop)**
```sh
scoop install openjdk21
```

Ensure Java 21 is active:
```sh
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
export PATH="$JAVA_HOME/bin:$PATH"
java -version
```

---

### **2. Install Gradle**
Ensure that **Gradle** is installed:

#### **macOS (Homebrew)**
```sh
brew install gradle
```

#### **Ubuntu (APT)**
```sh
sudo apt install gradle
```

#### **Windows (Scoop)**
```sh
scoop install gradle
```

Verify the installation:
```sh
gradle -v
```

---

### **3. Clone the Repository**
```sh
git clone git@github.com:AUnhurian/json2array-plugin.git
cd json2php-plugin
```

---

### **4. Initialize Gradle Wrapper**
Run the following command to generate the Gradle wrapper:
```sh
gradle wrapper
```
This will create the `./gradlew` script, ensuring the correct Gradle version is used.

---

## **Gradle Commands**

### **1. Clean Project**
```sh
./gradlew clean
```
Removes the `build/` directory and resets the project.

---

### **2. Build the Plugin**
```sh
./gradlew buildPlugin
```
Compiles the plugin and creates a distributable `.zip` file inside the `build/distributions/` folder.

---

### **3. Run Tests**
```sh
./gradlew test
```
Runs **JUnit 5 tests** to validate JSON-to-PHP array conversion.

---

### **4. Run Plugin in PhpStorm**
```sh
./gradlew runIde
```
Starts a **development instance** of PhpStorm **(2024.2.5)** with the plugin loaded for testing.

---

## **Usage**

### **JSON to PHP Array**
1. Select **JSON text** in PhpStorm.
2. Right-click and choose **"Convert JSON to PHP Array"**.
3. The **tool window** opens with:
   - **Input JSON**
   - **Converted PHP array**
   - **Replace in Editor** button
4. Modify or copy the output as needed.

### **PHP Array to JSON**
1. Open the **"Json to PHP Array"** tool window.
2. Paste your **PHP array** in the output field (or convert JSON first).
3. Click **"Convert to JSON"** button.
4. The JSON will appear in the input field.
5. Use **"Replace in Editor"** to replace selected text with JSON.

### **Features**
- ✅ **Bidirectional conversion**: JSON ↔ PHP Array
- ✅ **Nested objects and arrays** support
- ✅ **Boolean, null, and numeric values** handling
- ✅ **String escaping** for special characters
- ✅ **Quick conversion** via context menu
- ✅ **Tool window** for complex conversions
- ✅ **Replace in editor** functionality

---

## **Contributing**
- Fork the repository.
- Create a new branch (`feature/new-functionality`).
- Commit changes and submit a **pull request**.

---

## **License**
MIT License