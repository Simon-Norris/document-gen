# Mustache Template Guide

Mustache is a logic-less templating engine that works with many programming languages. This guide covers its syntax, best practices, and how to use it effectively.

## 1. Basic Syntax
Mustache uses double curly braces `{{ }}` to denote placeholders.

```mustache
Hello, {{name}}!
```

If `name = "John"`, the output will be:

```
Hello, John!
```

## 2. Variables
Variables are replaced with their corresponding values from the data model.

```mustache
Welcome, {{user.first_name}} {{user.last_name}}!
```

If the data is:

```json
{
  "user": {
    "first_name": "Alice",
    "last_name": "Smith"
  }
}
```

Output:

```
Welcome, Alice Smith!
```

## 3. Sections
Sections start with `{{#section}}` and end with `{{/section}}`. They are used for looping and conditionals.

### Looping Over Lists
```mustache
{{#items}}
  <li>{{name}} - {{price}}</li>
{{/items}}
```

For:

```json
{
  "items": [
    {"name": "Apple", "price": "$1"},
    {"name": "Banana", "price": "$0.5"}
  ]
}
```

Output:

```
<li>Apple - $1</li>
<li>Banana - $0.5</li>
```

### Conditionals
If a key has a `true` or non-empty value, the section will be rendered.

```mustache
{{#is_logged_in}}
  Welcome, {{username}}!
{{/is_logged_in}}
```

If `is_logged_in` is `true`, the output will be:

```
Welcome, JohnDoe!
```

## 4. Inverted Sections
Inverted sections use `{{^section}}` and render when the key is `false` or missing.

```mustache
{{^cart}}
  Your cart is empty.
{{/cart}}
```

If `cart` is empty or missing, the output will be:

```
Your cart is empty.
```

## 5. Partials (Templates Reuse)
Partials allow reusing template snippets.

```mustache
{{> header}}

Hello, {{name}}!

{{> footer}}
```

If `header.mustache` contains:

```
<header>Welcome to our site</header>
```

And `footer.mustache` contains:

```
<footer>Contact us at support@example.com</footer>
```

The final output will be:

```
<header>Welcome to our site</header>

Hello, John!

<footer>Contact us at support@example.com</footer>
```

## 6. Comments
Comments use `{{! }}` and are ignored in rendering.

```mustache
{{! This is a comment }}
```

## 7. Escaping HTML
Mustache escapes HTML by default. To prevent escaping, use triple curly braces `{{{ }}}`.

```mustache
{{{html_content}}}
```

For:

```json
{
  "html_content": "<b>Bold Text</b>"
}
```

Output:

```
<b>Bold Text</b>
```

If using `{{html_content}}`, it will be escaped:

```
&lt;b&gt;Bold Text&lt;/b&gt;
```

## 8. Delimiters
You can change delimiters using `{{=<% %>=}}`.

```mustache
<%#items%>
  <%name%>
<%/items%>
```

## 9. Best Practices
- Keep templates logic-less.
- Use partials for reusability.
- Structure data properly for easy rendering.
- Use sections for loops and conditionals.
- Be mindful of HTML escaping.

## Conclusion
Mustache is a simple and effective templating engine. Following these rules ensures clean, maintainable templates for dynamic content rendering.

