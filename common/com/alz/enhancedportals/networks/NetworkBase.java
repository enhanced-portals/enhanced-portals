package com.alz.enhancedportals.networks;

public class NetworkBase
{
    NetworkManager Parent;

    public NetworkBase(NetworkManager parent)
    {
        Parent = parent;
        loadData();
    }

    public void loadData()
    {
    }

    public void saveData()
    {
    }
}
