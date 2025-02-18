package com.document.generation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }


    String s = "Here’s the cleaned-up content with proper formatting and placeholders. You can copy it directly:\n" +
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
}
