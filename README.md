# JSON Filter

## Overview

This library provides a flexible way to filter [Gson](https://github.com/google/gson)'s 
[JsonElement](https://www.javadoc.io/doc/com.google.code.gson/gson/latest/com.google.gson/com/google/gson/JsonElement.html) 
objects by selectively including or excluding specific nodes. With support for nested filtering, it provides precise control 
over JSON structures using an intuitive and concise syntax.

## Filter Syntax

This library provides a simple yet powerful syntax for filtering JSON elements using inclusion and exclusion rules. 
The filtering syntax allows selecting or removing specific fields, including support for nested structures and arrays.

### **1. Inclusion Filter (`FilterType.INCLUSION`)**
An inclusion filter extracts only the specified fields from the JSON structure, removing all others.

#### **Example 1: Flat JSON Object**
**Filter:**
```plaintext
"a,b"
```
**Input JSON:**
```json
{"a":1,"b":2,"c":3}
```
**Filtered Output:**
```json
{"a":1,"b":2}
```

#### **Example 2: Nested JSON Object**
**Filter:**
```plaintext
"x(y)"
```
**Input JSON:**
```json
{"x":{"y":{"z":5},"w":10},"v":20}
```
**Filtered Output:**
```json
{"x":{"y":{"z":5}}}
```

#### **Example 3: Nested Array**
**Filter:**
```plaintext
"name"
```
**Input JSON:**
```json
[[[{"name":"john","type":0}]]]
```
**Filtered Output:**
```json
[[[{"name":"john"}]]]
```

### **2. Exclusion Filter (`FilterType.EXCLUSION`)**
An exclusion filter removes the specified fields while keeping all others.

#### **Example 1: Flat JSON Object**
**Filter:**
```plaintext
"a,b"
```
**Input JSON:**
```json
{"a":1,"b":2,"c":3}
```
**Filtered Output:**
```json
{"c":3}
```

#### **Example 2: Nested JSON Object**
**Filter:**
```plaintext
"x(y)"
```
**Input JSON:**
```json
{"x":{"y":{"z":5},"w":10},"v":20}
```
**Filtered Output:**
```json
{"x":{"w":10},"v":20}
```

#### **Example 3: Nested Array**
**Filter:**
```plaintext
"name"
```
**Input JSON:**
```json
[[[{"name":"john","type":0}]]]
```
**Filtered Output:**
```json
[[[{"type":0}]]]
```

This syntax allows precise control over JSON structures using simple and readable filtering rules.

## Usage

### Inclusion Filter

#### Example 1: Flat JSON Object

```java
// Parse a JSON string using JsonParser into a JsonElement instance.
JsonElement jsonElement = JsonParser.parseString("{\"a\":1,\"b\":2,\"c\":3}");

// Create an inclusion filter.
Filter filter = new FilterFactory().create(FilterType.INCLUSION);

// Apply the filter with "a,b" to the JsonElement instance.
JsonElement filtered = filter.apply(jsonElement, "a,b");

// {"a":1,"b":2}
System.out.println(filtered);
```

#### Example 2: Nested JSON Object

```java
// Parse a JSON string using JsonParser into a JsonElement instance.
JsonElement jsonElement = JsonParser.parseString("{\"x\":{\"y\":{\"z\":5},\"w\":10},\"v\":20}");

// Create an inclusion filter.
Filter filter = new FilterFactory().create(FilterType.INCLUSION);

// Apply the filter with "x(y)" to the JsonElement instance.
JsonElement filtered = filter.apply(jsonElement, "x(y)");

// {\"x\":{\"y\":{\"z\":5}}}
System.out.println(filtered);
```

#### Example 3: Nested Array

```java
// Parse a JSON string using JsonParser into a JsonElement instance.
JsonElement jsonElement = JsonParser.parseString("[[[{\"name\":\"john\",\"type\":0}]]]");

// Create an inclusion filter.
Filter filter = new FilterFactory().create(FilterType.INCLUSION);

// Apply the filter with "name" to the JsonElement instance.
JsonElement filtered = filter.apply(jsonElement, "name");

// [[[{"name":"john"}]]]
System.out.println(filtered);
```

### Exclusion Filter

#### Example 1: Flat JSON Object

```java
// Parse a JSON string using JsonParser into a JsonElement instance.
JsonElement jsonElement = JsonParser.parseString("{\"a\":1,\"b\":2,\"c\":3}");

// Create an exclusion filter.
Filter filter = new FilterFactory().create(FilterType.EXCLUSION);

// Apply the filter with "a,b" to the JsonElement instance.
JsonElement filtered = filter.apply(jsonElement, "a,b");

// {"c":3}
System.out.println(filtered);
```

#### Example 2: Nested JSON Object

```java
// Parse a JSON string using JsonParser into a JsonElement instance.
JsonElement jsonElement = JsonParser.parseString("{\"x\":{\"y\":{\"z\":5},\"w\":10},\"v\":20}");

// Create an exclusion filter.
Filter filter = new FilterFactory().create(FilterType.EXCLUSION);

// Apply the filter with "x(y)" to the JsonElement instance.
JsonElement filtered = filter.apply(jsonElement, "x(y)");

// {"x":{"w":10},"v":20}
System.out.println(filtered);
```

#### Example 3: Nested Array

```java
// Parse a JSON string using JsonParser into a JsonElement instance.
JsonElement jsonElement = JsonParser.parseString("[[[{\"name\":\"john\",\"type\":0}]]]");

// Create an exclusion filter.
Filter filter = new FilterFactory().create(FilterType.EXCLUSION);

// Apply the filter with "name" to the JsonElement instance.
JsonElement filtered = filter.apply(jsonElement, "name");

// [[[{"type":0}]]]
System.out.println(filtered);
```

## Installation

```xml
<dependency>
    <groupId>org.czeal</groupId>
    <artifactId>json-filter</artifactId>
    <version>0.0.1</version>
</dependency>
```

## License

Apache License, Version 2.0

## Java Doc

[JSON Filter JavaDoc](https://hidebike712.github.io/json-filter/)

## See Also

- [Gson](https://github.com/google/gson)
