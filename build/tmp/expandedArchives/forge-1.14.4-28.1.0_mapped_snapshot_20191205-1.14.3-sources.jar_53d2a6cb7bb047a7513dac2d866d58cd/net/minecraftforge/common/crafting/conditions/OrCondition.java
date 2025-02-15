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

package net.minecraftforge.common.crafting.conditions;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

public class OrCondition implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation("forge", "or");
    private final ICondition[] children;

    public OrCondition(ICondition... values)
    {
        if (values == null || values.length == 0)
            throw new IllegalArgumentException("Values must not be empty");

        for (ICondition child : values)
        {
            if (child == null)
                throw new IllegalArgumentException("Value must not be null");
        }

        this.children = values;
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test()
    {
        for (ICondition child : children)
        {
            if (child.test())
                return true;
        }

        return false;
    }

    @Override
    public String toString()
    {
        return Joiner.on(" || ").join(children);
    }

    public static class Serializer implements IConditionSerializer<OrCondition>
    {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, OrCondition value)
        {
            JsonArray values = new JsonArray();
            for (ICondition child : value.children)
                values.add(CraftingHelper.serialize(child));
            json.add("values", values);
        }

        @Override
        public OrCondition read(JsonObject json)
        {
            List<ICondition> children = new ArrayList<>();
            for (JsonElement j : JSONUtils.getJsonArray(json, "values"))
            {
                if (!j.isJsonObject())
                    throw new JsonSyntaxException("Or condition values must be an array of JsonObjects");
                children.add(CraftingHelper.getCondition(j.getAsJsonObject()));
            }
            return new OrCondition(children.toArray(new ICondition[children.size()]));
        }

        @Override
        public ResourceLocation getID()
        {
            return OrCondition.NAME;
        }
    }
}