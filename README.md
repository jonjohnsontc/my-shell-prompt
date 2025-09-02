# my-shell-prompt

This is a small script that can be compiled into an executable to set my shell prompt. I figured it'd be simple to dogfood this to learn more about Scala (+ Native).

Built using `scala-cli` 1.8.5`

Use `scala-cli .` to build and test output

Use `scala-cli package . -o <my-file>` to save as a native binary

Set as shell prompt by adding a function which invokes the binary. e.g.,

```bash
precmd() {
    PS1="$(/<path-to-this-repo>/<my-file>)"
}
```

