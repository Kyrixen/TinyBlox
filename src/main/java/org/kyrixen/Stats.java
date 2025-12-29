package org.kyrixen;


public class Stats {

    public interface Health {

        int getHealth();
        void setHealth(int health);
        void setMaxHealth(int maxHealth);

        boolean isDead();

        void kill();

        void invincible(boolean invincible);

        void damage(int damage);
        void heal(int amount);
        
    }


    public interface Stamina {

        int getStamina();
        void setStamina(int stamina);
        void setMaxStamina(int maxStamina);

        boolean isExhausted();

        void exhaust();

        void tireless(boolean tireless);    

        void consume(int amount);
        void recover(int amount);
    
        
    }
}

