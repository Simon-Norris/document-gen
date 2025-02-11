# **Comprehensive Guide to Freemarker (FTL) Templates**

---

## **🔹 1. Basics of Freemarker**
### **a) Comments**
```ftl
<#-- This is a comment -->
```
Comments are **not** included in the output.

### **b) Interpolation (Variable Substitution)**
```ftl
Hello, ${name}!
```
- If `name = "John"`, it outputs:  
  **Hello, John!**
- If `name` is **null**, it throws an error.  
  Use **`${name!}`** to prevent errors:
  ```ftl
  Hello, ${name!"Guest"}!
  ```
  Outputs: **Hello, Guest!** if `name` is null.

---

## **🔹 2. Control Structures**
### **a) Conditionals (`if`, `else`, `elseif`)**
```ftl
<#if age < 18>
  You are a minor.
<#elseif age >= 18 && age < 60>
  You are an adult.
<#else>
  You are a senior citizen.
</#if>
```

### **b) Switch Case (`choose`, `when`, `otherwise`)**
```ftl
<#switch category>
    <#case "electronics">
        You chose electronics.
        <#break>
    <#case "fashion">
        You chose fashion.
        <#break>
    <#default>
        Unknown category.
</#switch>
```

---

## **🔹 3. Loops (`list`, `foreach`)**
### **a) Iterating Over a List**
```ftl
<ul>
<#list items as item>
    <li>${item.name} - $${item.price}</li>
</#list>
</ul>
```

### **b) Checking if List is Empty**
```ftl
<#if items?size gt 0>
    <#list items as item>
        ${item.name}
    </#list>
<#else>
    No items available.
</#if>
```

### **c) Using Loop Index**
```ftl
<#list items as item>
    ${item_index + 1}. ${item.name}
</#list>
```
- `item_index` starts at **0**.
- Use `item?counter` to start at **1**.

---

## **🔹 4. Working with Strings**
### **a) String Operations**
```ftl
${"hello"?upper_case}  <!-- Output: HELLO -->
${"HELLO"?lower_case}  <!-- Output: hello -->
${"hello world"?capitalize}  <!-- Output: Hello world -->
```

### **b) Checking if a String Contains a Substring**
```ftl
<#if "hello world"?contains("world")>
    Yes, it contains "world".
</#if>
```

---

## **🔹 5. Handling Null & Default Values**
```ftl
${user.name!}  <!-- If null, outputs nothing -->
${user.name!"Guest"}  <!-- If null, outputs "Guest" -->
```

---

## **🔹 6. Date and Time Formatting**
```ftl
${date?date}  <!-- Converts string to date -->
${date?string("yyyy-MM-dd")}  <!-- Formats date -->
${.now}  <!-- Outputs current date/time -->
${.now?string("dd MMM yyyy")}  <!-- Formats current date -->
```

---

## **🔹 7. Functions in Freemarker**
### **a) Defining a Macro (Reusable Function)**
```ftl
<#macro greet userName>
    Hello, ${userName}!
</#macro>

<@greet userName="John"/>
```
Output: **Hello, John!**

### **b) Using Built-in Functions**
```ftl
${"hello world"?upper_case}  <!-- HELLO WORLD -->
${123.456?string("0.00")}  <!-- 123.46 -->
```

---

## **🔹 8. JSON & Data Transformation**
### **a) Converting JSON to Freemarker Map**
```ftl
${jsonString?eval}
```
If `jsonString = '{"name": "John"}'`, then `${jsonString?eval.name}` gives `"John"`.

---

## **🔹 9. File Handling (Classpath, External, String, Bytes)**
### **a) Loading a Template from Classpath**
```ftl
<#include "/templates/header.ftl">
```
OR
```ftl
<#import "/templates/functions.ftl" as func>
<@func.someFunction />
```

---

## **🔹 10. Debugging & Error Handling**
### **a) Checking If a Variable Exists**
```ftl
<#if someVariable??>
    ${someVariable}
<#else>
    Variable is missing.
</#if>
```

### **b) Logging for Debugging**
```ftl
<#assign debug = "Some debug message">
<#-- Print debug variable -->
${debug}
```

---

## **🔹 11. Advanced Use Cases**
### **a) Formatting Numbers**
```ftl
${price?string(",##0.00")}  <!-- Outputs: 1,234.56 -->
```

### **b) Escaping Special Characters**
```ftl
${"<script>alert('XSS');</script>"?html}
```
Outputs: `&lt;script&gt;alert('XSS');&lt;/script&gt;`

---

## **🔹 12. Performance Optimization Tips**
1. **Use `<#assign>` instead of inline calculations** for better efficiency.
2. **Avoid deep nesting** of conditionals and loops.
3. **Use macros** for repeated UI components.
4. **Leverage caching mechanisms** if templates are reused.

---

## **📌 Summary Table**
| Feature | Example |
|---------|---------|
| **Comments** | `<#-- This is a comment -->` |
| **Variables** | `${name}` |
| **Conditionals** | `<#if age > 18>Adult</#if>` |
| **Loops** | `<#list items as item>${item.name}</#list>` |
| **String Functions** | `"hello"?upper_case` |
| **Date Formatting** | `.now?string("yyyy-MM-dd")` |
| **Error Handling** | `<#if variable??>Exists</#if>` |
| **File Includes** | `<#include "header.ftl">` |
| **Debugging** | `${debug}` |

---

### 🚀 **With these rules, you can write clean, efficient, and powerful FTL templates!**
### Please visit <link>https://freemarker.apache.org/</link> for more information.

