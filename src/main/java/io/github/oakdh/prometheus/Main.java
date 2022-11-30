package io.github.oakdh.prometheus;

public class Main
{
    public static void main(String[] args)
    {
        if (!DatabaseHandler.connectDatabase()) return; // Stop het programma als er geen verbinding gemaakt kan worden met het database.

        HTTPHandler.initialize();
    }
}
