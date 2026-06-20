package io.kyrixen.tinyblox.entities.mob;


// Implementations for MobEntity.java
public class Stats {

    // Health
    public interface Health {

        float getHealth();
        void setHealth(float health);
        void setMaxHealth(int maxHealth);
        boolean isDead();

        void autoRegenerate(float delta);
        void setAutoRegenerate(boolean state);
        boolean getAutoRegenerate();

        void setInvincible(boolean invincible);
        boolean isInvincible();

        boolean damage(int damage);
        void heal(int amount);        
        void kill();

    }

    // Sprinting
    public interface Stamina {

        float getStamina();
        void setStamina(float stamina);
        void setMaxStamina(int maxStamina);
        boolean isExhausted();

        void autoRecover(float delta);
        void setAutoRecover(boolean state);
        boolean getAutoRecover();

        void setTireless(boolean tireless);    
        boolean isTireless();

        void consume(int amount);
        void recover(int amount);
        void exhaust();    
        
    }

}

