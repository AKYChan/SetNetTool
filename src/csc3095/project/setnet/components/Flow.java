package csc3095.project.setnet.components;

public enum Flow
{
    PLACE_TO_TRANSITION
            {
                @Override
                public final boolean canBeFired(Place p) { return p.hasToken(); }

                public final void performFire(Place p) { p.removeToken(); }
            },

    TRANSITION_TO_PLACE
            {
                public final void performFire(Place p) { p.addToken(); }
            };

    public boolean canBeFired(Place p) { return true; }
    public abstract void performFire(Place p);
}
