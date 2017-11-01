import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.GameObject;


import static org.dreambot.api.methods.Calculations.random;

@ScriptManifest(author = "zer0", name = "0-fishy", category = Category.FISHING, version = 0.1)

public class Main extends AbstractScript
{
    public boolean running = false;
    private State currentState;
    Area Fishy = new Area(0,0,0,0); //Add lumbridge cords
    GameObject fish;

    @Override
    public void onStart()
    {
        running = true;
        log("fishy is on");
        currentState = State.WAIT;
    }

    @Override
    public void onExit()
    {
        log("Thanks for using Fishy");
    }

    @Override
    public int onLoop()
    {

        currentState = getState();
        getState();
        switch(currentState)
        {
            case BANK:
                Bank();
                break;

            case FISH:
                Fish();
                break;

            case W_BANK:
                Walk_Bank();
                break;

            case W_FISH:
                Walk_Fish();
                break;

            case WAIT:
                sleep(2000);
                break;
        }
        return random(200,400);
    }

    private enum State
    {
        WAIT, FISH, W_FISH , BANK, W_BANK
    }

    private State getState()
    {
        if(getInventory().isFull() && BankLocation.LUMBRIDGE.getArea(5).contains(getLocalPlayer())) //Ready to bank
        {
            return State.BANK;
        }
        if(getInventory().isFull() && !BankLocation.LUMBRIDGE.getArea(5).contains(getLocalPlayer())) //Ready to walk to bank
        {
            return State.W_BANK;
        }
        if(!getInventory().isFull() && Fishy.contains(getLocalPlayer()))
        {
            return State.FISH;
        }
        if(!getInventory().isFull() && !Fishy.contains(getLocalPlayer()))
        {
            return State.W_FISH;
        }
        return State.WAIT;
    }

    public void Bank()
    {
        getBank().openClosest();
        getBank().depositAll("Shrimp");
    }

    public void Fish()
    {
        fish = getGameObjects().closest(f -> f != null  && f.getName().equalsIgnoreCase("Cabbage") && f.hasAction("Pick"));
        fish.interact();
        sleepUntil(() -> getLocalPlayer().isAnimating() || !fish.exists(),random(1500,2000));
        sleepUntil(() -> !getLocalPlayer().isAnimating() || !fish.exists(),random(1500,2000));
    }

    public void Walk_Bank()
    {
        getWalking().walk(BankLocation.LUMBRIDGE.getCenter());
    }

    public void Walk_Fish()
    {
        getWalking().walk(Fishy.getRandomTile());
    }

    /*  Todo

        Make progressive
        Fix dumb bugz
        Get better at code
        stop using states

     */
}
