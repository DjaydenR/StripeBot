package me.djaydenr.commands;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BetalingStarten extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("betaling")){
            if (event.getMember().hasPermission(Permission.ADMINISTRATOR)){
                Dotenv dotenv = Dotenv.load();
                Stripe.apiKey = dotenv.get("stripe-secret-key");

                JDA jda = event.getJDA();

                String prijs = String.valueOf(event.getOption("prijs").getAsInt());
                String naam = event.getOption("product").getAsString();



                try {
                   ProductCreateParams params2 =
                           ProductCreateParams
                                  .builder()
                                  .setName(naam)
                                  .build();

                   Product product = Product.create(params2);


                    PriceCreateParams params =
                            PriceCreateParams
                                    .builder()
                                    .setCurrency("eur")
                                    .setUnitAmount(Long.valueOf(prijs))
                                    .setProduct(product.getId())
                                    .build();

                    Price price = Price.create(params);

                    PaymentLinkCreateParams params1 =
                            PaymentLinkCreateParams
                                    .builder()
                                    .addLineItem(
                                            PaymentLinkCreateParams.LineItem.
                                                    builder()
                                                    .setPrice(price.getId())
                                                    .setQuantity(1L)
                                                    .build()
                                    )
                                    .build();

                    PaymentLink paymentLink = PaymentLink.create(params1);
                    event.reply(paymentLink.getUrl()).queue();

                } catch (StripeException e) {
                    throw new RuntimeException(e);
                }

            } else {
                event.reply("Jij hebt hier geen permissie voor").queue();
            }
        }
    }
}
