# Ornithe Standard Libraries

Ornithe Standard Libraries (OSL) provides tools for modding with Ornithe.

## Information for developers

Our [Ploceus gradle plugin](https://github.com/OrnitheMC/ploceus) provides helper methods for adding OSL dependencies to your projects:

```groovy
dependencies {
	...
	ploceus.dependOsl('0.8.0')
}
```

You can also add dependencies on individual OSL modules:

```groovy
dependencies {
	...
	ploceus.dependOslModule('core', '0.4.1')
	ploceus.dependOslModule('entrypoints', '0.4.0')
}
```
