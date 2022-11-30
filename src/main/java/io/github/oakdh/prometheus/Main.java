package io.github.oakdh.prometheus;

public class Main
{
    public static void main(String[] args)
    {
        if (!DatabaseHandler.connectDatabase()) return; // Stop het programma als er geen verbinding gemaakt kan worden met het database.
        
        DatabaseHandler.saveUserLogin(new UserLogin(12, "lol", "poopie", "lol@lol.lol"));

        HTTPHandler.initialize();
    }
}
