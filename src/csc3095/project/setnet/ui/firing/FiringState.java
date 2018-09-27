package csc3095.project.setnet.ui.firing;

import csc3095.project.setnet.main.Setnet;

public enum FiringState
{
    INACTIVE
            {
                @Override
                void run(Setnet setnet) {}

                @Override
                boolean canFire(Setnet setnet) { return false; }
            },

    SEQUENTIAL_FIRING
            {
                @Override
                void run(Setnet setnet) { setnet.seqFire(); }

                @Override
                boolean canFire(Setnet setnet) { return setnet.isSequentiallyFireable(); }
            },

    MAXIMAL_FIRING
            {
                @Override
                void run(Setnet setnet) { setnet.maxFire(); }

                @Override
                boolean canFire(Setnet setnet) { return setnet.isMaximallyFireable(); }
            };

    abstract void run(Setnet setnet);
    abstract boolean canFire(Setnet setnet);
}
