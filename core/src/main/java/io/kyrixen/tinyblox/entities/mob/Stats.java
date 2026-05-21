package io.kyrixen.tinyblox.entities.mob;


// Implementations for Entity.java
public class Stats {

    // Health
    public interface Health {

        float getHealth();
        void setHealth(int health);
        void setMaxHealth(int maxHealth);
        boolean isDead();

        void autoRegenerate(boolean state, float delta);
        void setInvincible(boolean invincible);

        boolean damage(int damage);
        void heal(int amount);        
        void kill();

    }

    // Sprinting
    public interface Stamina {

        float getStamina();
        void setStamina(int stamina);
        void setMaxStamina(int maxStamina);
        boolean isExhausted();

        void autoRecover(boolean state, float delta);
        void setTireless(boolean tireless);    

        void consume(int amount);
        void recover(int amount);
        void exhaust();    
        
    }

}

