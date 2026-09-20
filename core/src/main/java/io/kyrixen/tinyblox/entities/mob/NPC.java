package io.kyrixen.tinyblox.entities.mob;

import com.badlogic.gdx.Input;

import io.kyrixen.tinyblox.Constants;
import io.kyrixen.tinyblox.graphics.RendererStack;
import io.kyrixen.tinyblox.graphics.texture.TextureManager;
import io.kyrixen.tinyblox.menu.ui.Dialog;
import io.kyrixen.tinyblox.sound.SoundManager;
import io.kyrixen.tinyblox.utils.Peripheral;
import io.kyrixen.tinyblox.utils.TinyIdentifier;
import io.kyrixen.tinyblox.utils.TinyIdentifier.IdentifierType;
import io.kyrixen.tinyblox.world.Camera;
import io.kyrixen.tinyblox.world.Terrain;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer;
import io.kyrixen.tinyblox.world.chunk.tile.TileRenderer.FlipType;


// The most annoying one of all
public class NPC extends MobEntity {
    
    // The most useful feature of an NPC
    private final Dialog dialogue;

    // NPC needs camera and player (ugly but functional)
    private Camera camera = null;
    private Player player = null;

    // Dialog texture
    private static final TinyIdentifier DIALOG_TEXTURE = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "stone_dialog");


    public NPC(int x, int y, SoundManager soundManager) {
        super(x, y, soundManager);
        this.entityID = new TinyIdentifier("tinyblox", IdentifierType.ENTITY, "npc");
        this.dialogue = new Dialog(soundManager);
        this.dialogue.init((Constants.WINDOW_WIDTH - 100 * 5) / 2, 16, 100 * 5, 40 * 5);    
        this.dialogue.setLines(new String[]{"HELLO! (PRESS SPACE FOR EXIT)"});
        this.setInvincible(true);
    }

    public NPC(int id, float x, float y, SoundManager soundManager) {
        super(id, x, y, soundManager);
        this.entityID = new TinyIdentifier("tinyblox", IdentifierType.ENTITY, "npc");
        this.dialogue = new Dialog(soundManager);
        this.dialogue.init((Constants.WINDOW_WIDTH - 100 * 5) / 2, 16, 100 * 5, 40 * 5);
        this.dialogue.setLines(new String[]{"HELLO! (PRESS SPACE TO EXIT)"});
        this.setInvincible(true);
    }


    @Override 
    public void initTexture() {
        this.texture = new TinyIdentifier("tinyblox", IdentifierType.TEXTURE, "entity");
    }

    // Inits dialogue texture
    public void initDialogue(TextureManager tex) {
        this.dialogue.initTexture(tex.getTexture(DIALOG_TEXTURE));
    }


    @Override
    public void update(float deltaTime, Terrain terrain) {
        super.update(deltaTime, terrain);
        this.dialogue.updateState(deltaTime);

        if(dialogue.hasEnded()) { if(player.isInMenu()) player.toggleMenuStat(); }

        if(camera == null || player == null) return;
        if(!Peripheral.mouseJustPressed(Input.Buttons.LEFT)) return;

        int mouseX = Peripheral.getMouseX();
        int mouseY = Peripheral.getMouseY();

        float worldMouseX = mouseX / camera.zoom + camera.x;
        float worldMouseY = (Constants.WINDOW_HEIGHT - mouseY) / camera.zoom + camera.y;

        if(Math.abs(this.x() - player.x()) > Constants.GRID_SIZE * 2 || Math.abs(this.y() - player.y()) > Constants.GRID_SIZE * 2) return;
        if(worldMouseX < x() || worldMouseX > x() + width() || worldMouseY < y() || worldMouseY > y() + height()) return;

        dialogue.activate();
        if(!player.isInMenu()) player.toggleMenuStat();

    }

    @Override
    public void render(Terrain terrain, Player player, TileRenderer tileRenderer, RendererStack rendererStack) {
        
        if(this.camera == null) this.camera = rendererStack.camera;
        if(this.player == null) this.player = player;

        if(player.x() < this.x()) this.flip = FlipType.X_AXIS;
        else this.flip = FlipType.NONE;

        super.render(terrain, player, tileRenderer, rendererStack);
        
        tileRenderer.drawNameTag(this.x(), this.y(), "CLICK ME!", rendererStack);

    }

    // Renders NPC dialogue
    public void renderDialog(RendererStack rendererStack) {
        this.dialogue.render(rendererStack);
    }


    @Override 
    // Prevents normal flipping behavior
    protected void updateFlip() {}

    // Gets the NPC dialogue
    public Dialog getDialogue() { return this.dialogue; }


    @Override
    public String toString() {
        return "NPC(" + this.currentID + ")[" + this.entityID + "] { " + "x: " + this.x + ", y: " + this.y  + ", level: " + this.level + ", health: " + this.health + ", moving: " + Boolean.toString(this.moving) + " }";
    }

}
