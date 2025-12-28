# Bash completion for codeblueprint
# Source this file: source /path/to/codeblueprint.bash

_codeblueprint_completions() {
    local cur prev words cword
    _init_completion || return

    local commands="pattern algorithm help"
    local pattern_subcommands="list get search code"
    local algorithm_subcommands="list get search code"
    local pattern_categories="creational structural behavioral"
    local languages="kotlin java python swift javascript"

    case "${cword}" in
        1)
            COMPREPLY=($(compgen -W "${commands}" -- "${cur}"))
            ;;
        2)
            case "${prev}" in
                pattern)
                    COMPREPLY=($(compgen -W "${pattern_subcommands}" -- "${cur}"))
                    ;;
                algorithm)
                    COMPREPLY=($(compgen -W "${algorithm_subcommands}" -- "${cur}"))
                    ;;
            esac
            ;;
        3)
            case "${words[1]}" in
                pattern)
                    case "${prev}" in
                        list)
                            COMPREPLY=($(compgen -W "${pattern_categories}" -- "${cur}"))
                            ;;
                        get|search|code)
                            # 패턴 ID 자동 완성
                            local script_dir="$(dirname "$(which codeblueprint 2>/dev/null || echo ".")")"
                            local patterns_file="${script_dir}/../mcp-server/data/patterns.json"
                            if [[ -f "$patterns_file" ]] && command -v jq &>/dev/null; then
                                local pattern_ids=$(jq -r '.patterns[].id' "$patterns_file" 2>/dev/null)
                                COMPREPLY=($(compgen -W "${pattern_ids}" -- "${cur}"))
                            fi
                            ;;
                    esac
                    ;;
                algorithm)
                    case "${prev}" in
                        list)
                            # 알고리즘 카테고리 자동 완성
                            local script_dir="$(dirname "$(which codeblueprint 2>/dev/null || echo ".")")"
                            local algo_file="${script_dir}/../mcp-server/data/algorithms.json"
                            if [[ -f "$algo_file" ]] && command -v jq &>/dev/null; then
                                local categories=$(jq -r '.algorithms[].category' "$algo_file" 2>/dev/null | sort -u | tr '[:upper:]' '[:lower:]')
                                COMPREPLY=($(compgen -W "${categories}" -- "${cur}"))
                            fi
                            ;;
                        get|search|code)
                            # 알고리즘 ID 자동 완성
                            local script_dir="$(dirname "$(which codeblueprint 2>/dev/null || echo ".")")"
                            local algo_file="${script_dir}/../mcp-server/data/algorithms.json"
                            if [[ -f "$algo_file" ]] && command -v jq &>/dev/null; then
                                local algo_ids=$(jq -r '.algorithms[].id' "$algo_file" 2>/dev/null)
                                COMPREPLY=($(compgen -W "${algo_ids}" -- "${cur}"))
                            fi
                            ;;
                    esac
                    ;;
            esac
            ;;
        4)
            # 언어 자동 완성 (code 명령어일 때)
            if [[ "${words[2]}" == "code" ]]; then
                COMPREPLY=($(compgen -W "${languages}" -- "${cur}"))
            fi
            ;;
    esac
}

complete -F _codeblueprint_completions codeblueprint
