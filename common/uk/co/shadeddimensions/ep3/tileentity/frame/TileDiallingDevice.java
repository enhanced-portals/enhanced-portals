package uk.co.shadeddimensions.ep3.tileentity.frame;

import uk.co.shadeddimensions.ep3.tileentity.TilePortalPart;

public class TileDiallingDevice extends TilePortalPart
{

    public TileDiallingDevice()
    {

    }
    
    @Override
    public int getPowerDrainPerTick()
    {
        return 2;
    }

}
