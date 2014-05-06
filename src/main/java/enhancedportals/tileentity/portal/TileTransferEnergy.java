package enhancedportals.tileentity.portal;

import li.cil.oc.api.network.Arguments;
import li.cil.oc.api.network.Callback;
import li.cil.oc.api.network.Context;
import li.cil.oc.api.network.SimpleComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import enhancedportals.item.ItemPaintbrush;
import enhancedportals.network.CommonProxy;
import enhancedportals.network.GuiHandler;
import enhancedportals.utility.GeneralUtils;
import enhancedportals.utility.WorldUtils;

public class TileTransferEnergy extends TileFrameTransfer implements IEnergyHandler, IPowerReceptor, IPeripheral, SimpleComponent
{
    public final EnergyStorage storage = new EnergyStorage(16000);
    public final PowerHandler mjHandler;
    
    public TileTransferEnergy()
    {
        mjHandler = new PowerHandler(this, Type.MACHINE);
        mjHandler.configure(2.0f, 32.0f, 2.0f, (float)(storage.getMaxEnergyStored() / CommonProxy.RF_PER_MJ));
        mjHandler.configurePowerPerdition(0, 0);
    }

    @Override
    public boolean activate(EntityPlayer player, ItemStack stack)
    {
        if (player.isSneaking())
        {
            return false;
        }

        TileController controller = getPortalController();

        if (stack != null && controller != null && controller.isFinalized())
        {
            if (GeneralUtils.isWrench(stack))
            {
                GuiHandler.openGui(player, this, GuiHandler.TRANSFER_ENERGY);
                return true;
            }
            else if (stack.getItem() == ItemPaintbrush.instance)
            {
                GuiHandler.openGui(player, controller, GuiHandler.TEXTURE_A);
                return true;
            }
        }

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        storage.readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        storage.writeToNBT(nbt);
    }

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return storage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public boolean canInterface(ForgeDirection from)
    {
        return true;
    }

    @Override
    public int getEnergyStored(ForgeDirection from)
    {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(ForgeDirection from)
    {
        return storage.getMaxEnergyStored();
    }
    
    int tickTimer = 20, time = 0;
    
    @Override
    public void updateEntity()
    {
        super.updateEntity();
        
        if (!worldObj.isRemote)
        {
            if (isSending)
            {
                if (time >= tickTimer)
                {
                    time = 0;
                    
                    TileController controller = getPortalController();
                    
                    if (controller != null && controller.isPortalActive() && storage.getEnergyStored() > 0)
                    {
                        TileController exitController =  (TileController) controller.getDestinationLocation().getTileEntity();
                        
                        if (exitController != null)
                        {
                            for (ChunkCoordinates c : exitController.getTransferEnergy())
                            {
                                TileEntity tile = WorldUtils.getTileEntity(exitController.getWorldObj(), c);
                                
                                if (tile != null && tile instanceof TileTransferEnergy)
                                {
                                    TileTransferEnergy energy = (TileTransferEnergy) tile;
                                    
                                    if (!energy.isSending)
                                    {
                                        if (energy.receiveEnergy(null, storage.getEnergyStored(), true) > 0)
                                        {
                                            storage.extractEnergy(energy.receiveEnergy(null, storage.getEnergyStored(), false), false);
                                        }
                                    }
                                }
                                
                                if (storage.getEnergyStored() == 0)
                                {
                                    break;
                                }
                            }
                        }
                    }
                }
                
                time++;
            }
            else
            {
                if (!cached)
                {
                    updateEnergyHandlers();
                }
                
                for (int i = outputTracker; (i < 6) && (storage.getEnergyStored() > 0); i++)
                {
                    transferEnergy(i);
                }
                
                outputTracker++;
                outputTracker = (byte) (outputTracker % 6);
            }
        }
    }
    
    IEnergyHandler[] handlers = new IEnergyHandler[6];
    boolean cached = false;
    byte outputTracker = 0;
    
    @Override
    public void onNeighborChanged()
    {
        updateEnergyHandlers();
    }
    
    void transferEnergy(int side)
    {
        if (handlers[side] == null)
        {
            return;
        }
        
        storage.extractEnergy(handlers[side].receiveEnergy(ForgeDirection.getOrientation(side).getOpposite(), storage.getEnergyStored(), false), false);
    }
    
    void updateEnergyHandlers()
    {
        for (int i = 0; i < 6; i++)
        {
            TileEntity tile = WorldUtils.getTileEntity(this, ForgeDirection.getOrientation(i));
            
            if (tile != null && tile instanceof IEnergyHandler)
            {
                IEnergyHandler energy = (IEnergyHandler) tile;
                
                if (energy.canInterface(ForgeDirection.getOrientation(i).getOpposite()))
                {
                    handlers[i] = energy;
                }
                else
                {
                    handlers[i] = null;
                }
            }
            else
            {
                handlers[i] = null;
            }
        }
        
        cached = true;
    }

    @Override
    public String getType()
    {
        return "ETM";
    }

    @Override
    public String[] getMethodNames()
    {
        return new String[] { "getEnergyStored", "isFull", "isEmpty", "isSending" };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        if (method == 0)
        {
            return new Object[] { storage.getEnergyStored() };
        }
        else if (method == 1)
        {
            return new Object[] { storage.getEnergyStored() == storage.getMaxEnergyStored() };
        }
        else if (method == 2)
        {
            return new Object[] { storage.getEnergyStored() == 0 };
        }
        else if (method == 3)
        {
            return new Object[] { isSending };
        }
        
        return null;
    }

    @Override
    public void attach(IComputerAccess computer)
    {
        
    }

    @Override
    public void detach(IComputerAccess computer)
    {
        
    }

    @Override
    public PowerReceiver getPowerReceiver(ForgeDirection side)
    {
        return mjHandler.getPowerReceiver();
    }

    @Override
    public void doWork(PowerHandler workProvider)
    {
        int acceptedEnergy = storage.receiveEnergy((int)(mjHandler.useEnergy(1.0F, storage.getMaxEnergyStored() / CommonProxy.RF_PER_MJ, false) * CommonProxy.RF_PER_MJ), false);
        mjHandler.useEnergy(1.0F, acceptedEnergy / CommonProxy.RF_PER_MJ, true);
    }

    @Override
    public World getWorld()
    {
        return this.worldObj;
    }

    // OpenComputers
    
	@Override
	public String getComponentName() {
		return "ep_transfer_energy";
	}
	
	@Callback(direct = true)
	public Object[] getEnergy(Context context, Arguments args) {
		return new Object[]{storage.getEnergyStored()};
	}
	
	@Callback(direct = true)
	public Object[] getMaxEnergy(Context context, Arguments args) {
		return new Object[]{storage.getMaxEnergyStored()};
	}
	
	@Callback(direct = true)
	public Object[] isSending(Context context, Arguments args) {
		return new Object[]{isSending};
	}

	@Override
	public boolean equals(IPeripheral other)
	{
		return other == this;
	}
}
