package enhancedportals.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.network.PacketDispatcher;
import enhancedportals.EnhancedPortals;
import enhancedportals.lib.BlockIds;
import enhancedportals.lib.Localization;
import enhancedportals.lib.WorldLocation;
import enhancedportals.network.packet.PacketData;
import enhancedportals.network.packet.PacketTEUpdate;
import enhancedportals.portal.PortalTexture;
import enhancedportals.portal.network.DialDeviceNetworkObject;

public class TileEntityDialDevice extends TileEntityEnhancedPortals
{
    public ArrayList<DialDeviceNetworkObject> destinationList;
    public int selectedDestination;
    public boolean active;
    
    String oldModifierNetwork;
    PortalTexture oldModifierTexture;
    byte oldModifierThickness;
    boolean oldModifierParticles, oldModifierSounds;
    WorldLocation modifierLocation;
    
    public TileEntityDialDevice()
    {
        destinationList = new ArrayList<DialDeviceNetworkObject>();
        selectedDestination = -1;
        active = false;
    }

    @Override
    public PacketData getPacketData()
    {
        PacketData packetData = new PacketData(0, 1, destinationList.size());        
        packetData.byteData[0] = (byte) (active ? 1 : 0);
        
        for (int i = 0; i < destinationList.size(); i++)
        {
            DialDeviceNetworkObject obj = destinationList.get(i);            
            String str = obj.displayName + ";" + obj.network + ";" + obj.thickness + ";" + (obj.particles ? 1 : 0) + ";" + (obj.sounds ? 1 : 0);
        
            if (obj.texture.colour != -1)
            {
                str = str + ";" + obj.texture.colour;
            }
            else if (obj.texture.blockID != -1)
            {
                str = str + ";" + obj.texture.blockID + ";" + obj.texture.metaData;
            }
            else
            {
                str = str + ";" + obj.texture.liquidID;
            }
            
            packetData.stringData[i] = str;
        }
        
        return packetData;
    }

    @Override
    public void parsePacketData(PacketData data)
    {
        destinationList.clear();
        active = data.byteData[0] == 1;
        
        for (int i = 0; i < data.stringData.length; i++)
        {            
            if (data.stringData[i] == null)
            {
                continue;
            }
            
            String[] split = data.stringData[i].split(";");
            PortalTexture tex = null;
            
            if (split.length == 6)
            {
                if (split[5].equals("0") || split[5].equals("1") || split[5].equals("2") || split[5].equals("3") || split[5].equals("4"))
                {
                    tex = new PortalTexture(Byte.parseByte(split[5]));
                }
                else
                {
                    tex = new PortalTexture(split[5]);
                }
            }
            else if (split.length == 7)
            {
                tex = new PortalTexture(Integer.parseInt(split[5]), Integer.parseInt(split[6]));
            }
            
            destinationList.add(new DialDeviceNetworkObject(split[0], split[1], tex, Byte.parseByte(split[2]), split[3].equals("1"), split[4].equals("1")));            
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tagCompound)
    {
        super.readFromNBT(tagCompound);
        
        if (tagCompound.hasKey("OldNetwork"))
        {
            active = true;
            oldModifierNetwork = tagCompound.getString("OldNetwork");
            oldModifierThickness = tagCompound.getByte("OldThickness");
            oldModifierParticles = tagCompound.getBoolean("OldParticles");
            oldModifierSounds = tagCompound.getBoolean("OldSounds");
            modifierLocation = new WorldLocation(tagCompound.getInteger("mX"), tagCompound.getInteger("mY"), tagCompound.getInteger("mZ"), tagCompound.getInteger("mD"));
            
            if (tagCompound.hasKey("OldTextureColour"))
            {
                oldModifierTexture = new PortalTexture(tagCompound.getByte("OldTextureColour"));
            }
            else if (tagCompound.hasKey("OldTextureBlock"))
            {
                oldModifierTexture = new PortalTexture(tagCompound.getInteger("OldTextureBlock"), tagCompound.getInteger("OldTextureMeta"));
            }
            else if (tagCompound.hasKey("OldTextureLiquid"))
            {
                oldModifierTexture = new PortalTexture(tagCompound.getString("OldTextureLiquid"));
            }
        }
        
        NBTTagList list = tagCompound.getTagList("Entries");
        
        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound tag = (NBTTagCompound) list.tagAt(i);
            
            String name = tag.getString("Name"),
                   network = tag.getString("Network");
            byte thickness = tag.getByte("Thickness");
            boolean particle = tag.getBoolean("Particles"),
                    sound = tag.getBoolean("Sounds");
            PortalTexture texture = null;            
            
            if (tag.hasKey("TextureColour"))
            {
                texture = new PortalTexture(tag.getByte("TextureColour"));
            }
            else if (tag.hasKey("TextureBlock"))
            {
                texture = new PortalTexture(tag.getInteger("TextureBlock"), tag.getInteger("TextureMeta"));
            }
            else if (tag.hasKey("TextureLiquid"))
            {
                texture = new PortalTexture(tag.getString("TextureLiquid"));
            }
            
            destinationList.add(new DialDeviceNetworkObject(name, network, texture, thickness, sound, particle));
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        
        if (active)
        {
            tagCompound.setString("OldNetwork", oldModifierNetwork);
            tagCompound.setByte("OldThickness", oldModifierThickness);
            tagCompound.setBoolean("OldParticles", oldModifierParticles);
            tagCompound.setBoolean("OldSounds", oldModifierSounds);
            tagCompound.setInteger("mX", modifierLocation.xCoord);
            tagCompound.setInteger("mY", modifierLocation.yCoord);
            tagCompound.setInteger("mZ", modifierLocation.zCoord);
            tagCompound.setInteger("mD", modifierLocation.dimension);
            
            if (oldModifierTexture.colour != -1)
            {
                tagCompound.setByte("OldTextureColour", oldModifierTexture.colour);
            }
            else if (oldModifierTexture.blockID != -1)
            {
                tagCompound.setInteger("OldTextureBlock", oldModifierTexture.blockID);
                tagCompound.setInteger("OldTextureMeta", oldModifierTexture.metaData);
            }
            else
            {
                tagCompound.setString("OldTextureLiquid", oldModifierTexture.liquidID);
            }
        }
        
        NBTTagList list = new NBTTagList();
        
        for (int i = 0; i < destinationList.size(); i++)
        {
            NBTTagCompound tag = new NBTTagCompound();
            DialDeviceNetworkObject obj = destinationList.get(i);
            
            if (obj == null)
            {
                continue;
            }
            
            tag.setString("Name", obj.displayName);
            tag.setString("Network", obj.network);
            tag.setByte("Thickness", obj.thickness);
            tag.setBoolean("Particles", obj.particles);
            tag.setBoolean("Sounds", obj.sounds);
            
            if (obj.texture.colour != -1)
            {
                tag.setByte("TextureColour", obj.texture.colour);
            }
            else if (obj.texture.blockID != -1)
            {
                tag.setInteger("TextureBlock", obj.texture.blockID);
                tag.setInteger("TextureMeta", obj.texture.metaData);
            }
            else
            {
                tag.setString("TextureLiquid", obj.texture.liquidID);
            }
            
            list.appendTag(tag);
        }
        
        tagCompound.setTag("Entries", list);
    }
    
    public void processDiallingRequest(String network, EntityPlayer player, PortalTexture texture, byte thickness, boolean particles, boolean sound)
    {
        if (worldObj.isRemote || active)
        {
            return;
        }

        outerloop: for (int x = -5; x < 5; x++)
        {
            for (int z = -5; z < 5; z++)
            {
                for (int y = -5; y < 5; y++)
                {
                    if (worldObj.getBlockId(xCoord + x, yCoord + y, zCoord + z) == BlockIds.PortalModifier)
                    {
                        modifierLocation = new WorldLocation(xCoord + x, yCoord + y, zCoord + z, worldObj);
                        break outerloop;
                    }
                }
            }
        }

        if (modifierLocation == null)
        {
            if (player != null)
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noModifier"));
            }

            return;
        }

        if (EnhancedPortals.proxy.ModifierNetwork.hasNetwork(network))
        {
            if (modifierLocation.getBlockId() == BlockIds.PortalModifier)
            {
                TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();

                oldModifierNetwork = modifier.network;
                oldModifierParticles = modifier.getParticles();
                oldModifierSounds = modifier.getSounds();
                oldModifierTexture = modifier.texture;
                oldModifierThickness = modifier.thickness;
                
                EnhancedPortals.proxy.ModifierNetwork.removeFromAllNetworks(modifierLocation);
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(network, modifierLocation);
                
                modifier.network = network;
                modifier.texture = texture;
                modifier.thickness = thickness;
                modifier.setSounds(sound);
                modifier.setParticles(particles);
                
                PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(modifier).getPacket());

                if (modifier.isAnyActive())
                {
                    modifier.removePortal();
                }

                if (modifier.createPortal())
                {
                    if (player != null)
                    {
                        player.sendChatToPlayer(EnumChatFormatting.GREEN + Localization.localizeString("chat.dialSuccess"));
                    }

                    worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, BlockIds.DialHomeDevice, 760);
                    active = true;
                    PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
                }
                else
                {
                    modifier.network = oldModifierNetwork;

                    if (player != null)
                    {
                        player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noPortal"));
                    }
                }
            }
            else if (player != null)
            {
                player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"));
            }
        }
        else if (player != null)
        {
            player.sendChatToPlayer(EnumChatFormatting.RED + Localization.localizeString("chat.noConnection"));
        }
    }

    public void scheduledBlockUpdate()
    {
        if (modifierLocation == null)
        {
            active = false;
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
            return;
        }

        if (modifierLocation.getBlockId() == BlockIds.PortalModifier)
        {
            TileEntityPortalModifier modifier = (TileEntityPortalModifier) modifierLocation.getTileEntity();

            EnhancedPortals.proxy.ModifierNetwork.removeFromNetwork(modifier.network, modifierLocation);

            if (oldModifierNetwork != null && !oldModifierNetwork.equals(""))
            {
                EnhancedPortals.proxy.ModifierNetwork.addToNetwork(oldModifierNetwork, modifierLocation);
            }

            modifier.removePortal();
            modifier.network = oldModifierNetwork;
            modifier.setParticles(oldModifierParticles);
            modifier.setSounds(oldModifierSounds);
            modifier.texture = oldModifierTexture;
            modifier.thickness = oldModifierThickness;            
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(modifier).getPacket());
            
            oldModifierNetwork = "";
            modifierLocation = null;
            oldModifierParticles = false;
            oldModifierSounds = false;
            oldModifierTexture = null;
            oldModifierThickness = 0;
            active = false;
            PacketDispatcher.sendPacketToAllAround(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 128, worldObj.provider.dimensionId, new PacketTEUpdate(this).getPacket());
        }
    }
}
