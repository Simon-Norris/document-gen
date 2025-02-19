package com.document.generation.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FreeMarkerRendererTest {

    private FreeMarkerRenderer freemarkerRenderer;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        freemarkerRenderer = new FreeMarkerRenderer(objectMapper);
    }

    @Test
    public void testRenderTemplateStringCase() {
        String templateString = "Hello, ${name}! You have ${count} unread messages.";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(templateString, dataModel);

        assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
    }

    @Test
    public void testRenderTemplateByteCase() {
        String templateString = "Here’s the cleaned-up content with proper formatting and placeholders. You can copy it directly:\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Hello,**  \n" +
                "<#if (user.gender == \"male\")>  \n" +
                "  Mr. ${user.firstName} ${user.lastName}  \n" +
                "<#elseif (user.gender == \"female\")>  \n" +
                "  Ms. ${user.firstName} ${user.lastName}  \n" +
                "<#else>  \n" +
                "  ${user.firstName} ${user.lastName}  \n" +
                "</#if>\n" +
                "\n" +
                "**Welcome to our platform, ${user.username}!**\n" +
                "\n" +
                "<#if (user.age < 18)>  \n" +
                "  You are a minor. Parental guidance is required.  \n" +
                "<#elseif (user.age <= 60)>  \n" +
                "  You are an adult with full access.  \n" +
                "<#else>  \n" +
                "  You are a senior citizen. Enjoy our special discounts!  \n" +
                "</#if>\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Your Profile Summary**\n" +
                "\n" +
                "- **Email:** ${user.email}  \n" +
                "- **Member Since:** ${user.registrationDate?date(\"yyyy-MM-dd\")}  \n" +
                "- **Subscription Plan:** ${user.subscriptionPlan}\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Your Recent Orders**\n" +
                "\n" +
                "| #   | Item        | Quantity | Price   | Status   |  \n" +
                "|-----|-------------|----------|---------|----------|  \n" +
                "<#list orders as order>  \n" +
                "| ${order_index + 1} | ${order.itemName} | ${order.quantity} | $${order.price?string(\"0.00\")} |  \n" +
                "  <#if (order.status == \"shipped\")>Shipped  \n" +
                "  <#elseif (order.status == \"pending\")>Pending  \n" +
                "  <#elseif (order.status == \"cancelled\")>Cancelled  \n" +
                "  <#else>Unknown</#if> |  \n" +
                "</#list>\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Notifications**\n" +
                "\n" +
                "<#list notifications as notification>  \n" +
                "- **${notification.title}** - ${notification.date?date(\"yyyy-MM-dd\")}  \n" +
                "  ${notification.message}  \n" +
                "</#list>\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Exclusive Offers for You**\n" +
                "\n" +
                "<#if (promotions?size == 0)>  \n" +
                "  No special offers at the moment. Check back later!  \n" +
                "<#else>  \n" +
                "  <#list promotions as promo>  \n" +
                "    - **${promo.title}**: ${promo.description} (Valid until ${promo.expiryDate?date(\"yyyy-MM-dd\")})  \n" +
                "  </#list>  \n" +
                "</#if>\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Thank you for being a valued customer, ${user.firstName}!**\n" +
                "\n" +
                "---\n" +
                "\n" +
                "This is now structured clearly and ready for you to copy. Let me know if this works or if you need any further modifications!";

        Map<String, Object> dataModel = new HashMap<>();

        dataModel.put("user", Map.of(
                "firstName", "John",
                "lastName", "Doe",
                "gender", "male",
                "username", "johndoe",
                "age", 30,
                "email", "john.doe@example.com",
                "registrationDate", "2024-02-18",
                "subscriptionPlan", "Premium"
        ));

        List<Map<String, Object>> orders = List.of(
                Map.of("itemName", "Laptop", "quantity", 1, "price", 1200.00, "status", "shipped"),
                Map.of("itemName", "Phone", "quantity", 2, "price", 800.00, "status", "pending"),
                Map.of("itemName", "Headphones", "quantity", 1, "price", 150.00, "status", "cancelled")
        );
        dataModel.put("orders", orders);

        List<Map<String, Object>> notifications = List.of(
                Map.of("title", "Order Shipped", "date", "2024-02-15", "message", "Your laptop has been shipped."),
                Map.of("title", "Payment Reminder", "date", "2024-02-14", "message", "Your subscription payment is due.")
        );
        dataModel.put("notifications", notifications);

        List<Map<String, Object>> promotions = List.of(
                Map.of("title", "Spring Sale", "description", "Get 20% off on all electronics!", "expiryDate", "2024-03-01"),
                Map.of("title", "Membership Upgrade", "description", "Upgrade to Platinum and get 1 month free!", "expiryDate", "2024-02-28")
        );
        dataModel.put("promotions", promotions);


        String renderedOutput = freemarkerRenderer.render(templateString.getBytes(), dataModel);

        String expectedOutput = "Here’s the cleaned-up content with proper formatting and placeholders. You can copy it directly:\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Hello,**  \n" +
                "  Mr. John Doe  \n" +
                "\n" +
                "**Welcome to our platform, johndoe!**\n" +
                "\n" +
                "  You are an adult with full access.  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Your Profile Summary**\n" +
                "\n" +
                "- **Email:** john.doe@example.com  \n" +
                "- **Member Since:** Feb 18, 2024  \n" +
                "- **Subscription Plan:** Premium\n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Your Recent Orders**\n" +
                "\n" +
                "| #   | Item        | Quantity | Price   | Status   |  \n" +
                "|-----|-------------|----------|---------|----------|  \n" +
                "| 1 | Laptop | 1 | $1200.00 |  \n" +
                "  Shipped  \n" +
                "   |  \n" +
                "| 2 | Phone | 2 | $800.00 |  \n" +
                "  Pending  \n" +
                "   |  \n" +
                "| 3 | Headphones | 1 | $150.00 |  \n" +
                "  Cancelled  \n" +
                "   |  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Notifications**\n" +
                "\n" +
                "- **Order Shipped** - Feb 15, 2024  \n" +
                "  Your laptop has been shipped.  \n" +
                "- **Payment Reminder** - Feb 14, 2024  \n" +
                "  Your subscription payment is due.  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Exclusive Offers for You**\n" +
                "\n" +
                "    - **Spring Sale**: Get 20% off on all electronics! (Valid until Mar 1, 2024)  \n" +
                "    - **Membership Upgrade**: Upgrade to Platinum and get 1 month free! (Valid until Feb 28, 2024)  \n" +
                "\n" +
                "---\n" +
                "\n" +
                "**Thank you for being a valued customer, John!**\n" +
                "\n" +
                "---\n" +
                "\n" +
                "This is now structured clearly and ready for you to copy. Let me know if this works or if you need any further modifications!";

        assertEquals(expectedOutput, renderedOutput);
    }

    @Test
    public void testRenderTemplateFileCase(@TempDir File tempDir) throws IOException {
        File tempFile = new File(tempDir, "template.html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Hello, ${name}! You have ${count} unread messages.");
        }

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(tempFile, dataModel);

        assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
    }

    @Test
    public void testRenderTemplateInputStreamCase(@TempDir File tempDir) throws IOException {
        File tempFile = new File(tempDir, "template.html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write("Hello, ${name}! You have ${count} unread messages.");
        }

        try (InputStream inputStream = new FileInputStream(tempFile)) {
            Map<String, Object> dataModel = new HashMap<>();
            dataModel.put("name", "John");
            dataModel.put("count", 5);

            String renderedOutput = freemarkerRenderer.render(inputStream, dataModel);

            assertEquals("Hello, John! You have 5 unread messages.", renderedOutput);
        }
    }

    @Test
    public void testRenderTemplateClasspathResource() {
        String templatePath = "classpath:templates/hello.ftl";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("name", "John");
        dataModel.put("count", 5);

        String renderedOutput = freemarkerRenderer.render(templatePath, dataModel);

        assertEquals("Hello Dear John! You have 5 unread messages.\r\n", renderedOutput);
    }

    @Test
    public void testRenderTemplateWithInvalidData() {
        String templateString = "Hello, ${name}!";

        Map<String, Object> dataModel = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> {
            freemarkerRenderer.render(templateString.getBytes(), dataModel);
        });
    }

    @Test
    public void testRenderTemplateWhiteSpaceStrSuccess() {
        String templateString = "<p>Hello, ${name} <#if (age < 18)> You are a minor. <#elseif (age <= 60)> You are an adult. <#else> You are a senior citizen. </#if> Items: <#list items as item> ${item_index + 1}. ${item.name} </#list></p>";

        Map<String, Object> dataModel = new HashMap<>();

        List<Map<String, Object>> items = new ArrayList<>();

        items.add(Map.of("name", "Watch"));
        items.add(Map.of("name", "Earbuds"));
        items.add(Map.of("name", "Laptop"));

        dataModel.put("name", "John");
        dataModel.put("age", 25);
        dataModel.put("items", items);

        String renderedOutput = freemarkerRenderer.render(templateString.getBytes(), dataModel);

        String expectedOutput = "<p>Hello, John  You are an adult.  Items:  1. Watch  2. Earbuds  3. Laptop </p>";

        assertEquals(expectedOutput, renderedOutput);
    }
}
