/*
 * Minecraft Forge
 * Copyright (c) 2016-2019.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.minecraftforge.client.model;

import com.google.common.base.Objects;
import net.minecraft.client.renderer.texture.ISprite;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Optional;

/**
 * An {@link IModelState} that combines the transforms from two child {@link IModelState}.
 */
public class ModelStateComposition implements IModelState, ISprite
{
    private final IModelState first;
    private final IModelState second;
    private final boolean uvLock;

    public ModelStateComposition(IModelState first, IModelState second)
    {
        this(first, second, false);
    }

    public ModelStateComposition(IModelState first, IModelState second, boolean uvLock)
    {
        this.first = first;
        this.second = second;
        this.uvLock = uvLock;
    }

    @Override
    public IModelState getState()
    {
        return this;
    }

    @Override
    public boolean isUvLock()
    {
        return uvLock;
    }

    @Override
    public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part)
    {
        Optional<TRSRTransformation> f = first.apply(part), s = second.apply(part);
        if(f.isPresent() && s.isPresent())
        {
            return Optional.of(f.get().compose(s.get()));
        }
        if (f.isPresent()) {
            return f;
        }
        return s;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ModelStateComposition that = (ModelStateComposition) o;
        return Objects.equal(first, that.first) && Objects.equal(second, that.second);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(first, second);
    }
}