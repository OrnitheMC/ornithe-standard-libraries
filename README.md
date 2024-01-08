# Ornithe Standard Libraries

Ornithe Standard Libraries (OSL) provides tools for modding with Ornithe.

## Information for developers

Our [Ploceus gradle plugin](https://github.com/OrnitheMC/ploceus) provides helper methods for adding OSL dependencies to your projects:

```groovy
dependencies {
	...
	ploceus.dependOsl('0.12.0')
}
```

The default game side is merged (`"*"`), but you can specify a custom value. In projects for MC 1.3+, always use merged, and in projects for MC -1.3, use the client and server sides.

```groovy
dependencies {
	...
	ploceus.dependOsl('0.12.0', 'client')
}
```
```groovy
import net.ornithemc.ploceus.GameSide;

dependencies {
	...
	ploceus.dependOsl('0.12.0', GameSide.CLIENT)
}
```

By default the dependencies are added to the `modImplementation` configuration, but you can specify a custom value:

```groovy
dependencies {
	...
	ploceus.dependOsl('modCompileOnly', '0.12.0', 'client')
}
```

You can also add dependencies on individual OSL modules:

```groovy
dependencies {
	...
	ploceus.dependOslModule('core', '0.5.0')
	ploceus.dependOslModule('entrypoints', '0.4.2')
}
```

The default game side is merged (`"*"`), but you can specify a custom value. In projects for MC 1.3+, always use merged, and in projects for MC -1.3, use the client and server sides.

```groovy
dependencies {
	...
	ploceus.dependOslModule('core', '0.5.0', 'client')
	ploceus.dependOslModule('entrypoints', '0.4.2', 'client')
}
```
```groovy
import net.ornithemc.ploceus.GameSide;

dependencies {
	...
	ploceus.dependOslModule('core', '0.5.0', GameSide.CLIENT)
	ploceus.dependOslModule('entrypoints', '0.4.2', GameSide.CLIENT)
}
```

By default the dependencies are added to the `modImplementation` configuration, but you can specify a custom value:

```groovy
dependencies {
	...
	ploceus.dependOslModule('modCompileOnly', 'core', '0.5.0', 'client')
	ploceus.dependOslModule('modCompileOnly', 'entrypoints', '0.4.2', 'client')
}
```

If you require the specific version number for an OSL module, you can query it:

```groovy
def coreVersion = ploceus.oslModule('core', '0.5.0')
```

The default game side is merged (`"*"`), but you can specify a custom value. In projects for MC 1.3+, always use merged, and in projects for MC -1.3, use the client and server sides.

```groovy
def coreVersion = ploceus.oslModule('core', '0.5.0', 'client')
```
```groovy
import net.ornithemc.ploceus.GameSide;

def coreVersion = ploceus.oslModule('core', '0.5.0', GameSide.CLIENT)
```
