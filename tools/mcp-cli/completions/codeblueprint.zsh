#compdef codeblueprint

# Zsh completion for codeblueprint
# Source this file: source /path/to/codeblueprint.zsh

_codeblueprint() {
    local curcontext="$curcontext" state line
    typeset -A opt_args

    local script_dir="${0:A:h}/.."
    local patterns_file="${script_dir}/../mcp-server/data/patterns.json"
    local algo_file="${script_dir}/../mcp-server/data/algorithms.json"

    _arguments -C \
        '1: :->command' \
        '2: :->subcommand' \
        '3: :->arg1' \
        '4: :->arg2'

    case "$state" in
        command)
            local commands=(
                'pattern:패턴 관련 명령어'
                'algorithm:알고리즘 관련 명령어'
                'help:도움말 표시'
            )
            _describe 'command' commands
            ;;
        subcommand)
            case "${words[2]}" in
                pattern)
                    local subcommands=(
                        'list:패턴 목록 조회'
                        'get:패턴 상세 조회'
                        'search:패턴 검색'
                        'code:코드 예시 조회'
                    )
                    _describe 'subcommand' subcommands
                    ;;
                algorithm)
                    local subcommands=(
                        'list:알고리즘 목록 조회'
                        'get:알고리즘 상세 조회'
                        'search:알고리즘 검색'
                        'code:코드 예시 조회'
                    )
                    _describe 'subcommand' subcommands
                    ;;
            esac
            ;;
        arg1)
            case "${words[2]}" in
                pattern)
                    case "${words[3]}" in
                        list)
                            local categories=('creational' 'structural' 'behavioral')
                            _describe 'category' categories
                            ;;
                        get|code)
                            if [[ -f "$patterns_file" ]] && command -v jq &>/dev/null; then
                                local pattern_ids=(${(f)"$(jq -r '.patterns[].id' "$patterns_file" 2>/dev/null)"})
                                _describe 'pattern_id' pattern_ids
                            fi
                            ;;
                        search)
                            _message 'search keyword'
                            ;;
                    esac
                    ;;
                algorithm)
                    case "${words[3]}" in
                        list)
                            if [[ -f "$algo_file" ]] && command -v jq &>/dev/null; then
                                local categories=(${(f)"$(jq -r '.algorithms[].category' "$algo_file" 2>/dev/null | sort -u | tr '[:upper:]' '[:lower:]')"})
                                _describe 'category' categories
                            fi
                            ;;
                        get|code)
                            if [[ -f "$algo_file" ]] && command -v jq &>/dev/null; then
                                local algo_ids=(${(f)"$(jq -r '.algorithms[].id' "$algo_file" 2>/dev/null)"})
                                _describe 'algorithm_id' algo_ids
                            fi
                            ;;
                        search)
                            _message 'search keyword'
                            ;;
                    esac
                    ;;
            esac
            ;;
        arg2)
            if [[ "${words[3]}" == "code" ]]; then
                local languages=('kotlin' 'java' 'python' 'swift' 'javascript')
                _describe 'language' languages
            fi
            ;;
    esac
}

_codeblueprint
