/*
 *  This file is part of Cubic Chunks Mod, licensed under the MIT License (MIT).
 *
 *  Copyright (c) 2015 contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package cubicchunks.worldgen.generator.custom;

import com.google.common.collect.Sets;
import cubicchunks.util.Coords;
import cubicchunks.util.processor.CubeProcessor;
import cubicchunks.world.ICubicWorld;
import cubicchunks.world.cube.Cube;
import cubicchunks.worldgen.GeneratorStage;
import cubicchunks.worldgen.generator.custom.features.BiomeFeatures;
import cubicchunks.worldgen.generator.custom.features.FeatureGenerator;
import net.minecraft.world.biome.BiomeGenBase;

import java.util.*;

public class CustomPopulationProcessor extends CubeProcessor {

	private Map<BiomeGenBase, BiomeFeatures> biomeFeaturesMap;

	public CustomPopulationProcessor(String name, ICubicWorld world, int batchSize) {
		super(name, world.getCubeCache(), batchSize);

		this.biomeFeaturesMap = new HashMap<>();

		// for now use global for all biomes
		for (BiomeGenBase biome : BiomeGenBase.REGISTRY) {
			if(biome == null){
				continue;
			}
			this.biomeFeaturesMap.put(biome, new BiomeFeatures(world, biome));
		}
	}

	@Override
	public Set<Cube> calculate(Cube cube) {
		if(true)return Sets.newHashSet(cube);
		if (!cube.getWorld().cubeAndNeighborsExist(cube, true, GeneratorStage.POPULATION)) {
			return Collections.EMPTY_SET;
		}

		BiomeGenBase biome = cube.getWorld().getBiomeGenForCoords(Coords.getCubeCenter(cube));
    
		//For surface generators we should actually use special RNG with seed 
		//that depends only in world seed and cube X/Z
		//but using this for surface generation doesn't cause any noticable issues
		Random rand = new Random(cube.cubeRandomSeed());
		
		BiomeFeatures features = this.biomeFeaturesMap.get(biome);
		for (FeatureGenerator gen : features.getBiomeFeatureGenerators()) {
			gen.generate(rand, cube, biome);
		}

		return Sets.newHashSet(cube);
	}
}
